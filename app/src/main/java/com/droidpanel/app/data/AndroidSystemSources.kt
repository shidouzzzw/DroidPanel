package com.droidpanel.app.data

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import java.io.File

/**
 * Android-backed readers for the live modules. Everything here is public API and needs no
 * permission: memory and storage are per-process readings, the battery broadcast is sticky and
 * readable by any app, and /sys CPU frequencies are world-readable on most kernels.
 */
class AndroidMemorySource(private val context: Context) {
    fun read(): MemoryStats {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(info)
        val runtime = Runtime.getRuntime()
        return MemoryStats(
            totalBytes = info.totalMem,
            availableBytes = info.availMem,
            thresholdBytes = info.threshold,
            lowMemory = info.lowMemory,
            appHeapUsedBytes = (runtime.totalMemory() - runtime.freeMemory()).coerceAtLeast(0L),
            appHeapMaxBytes = runtime.maxMemory(),
        )
    }
}

class AndroidBatterySource(private val context: Context) {
    fun read(): BatteryStats {
        // Sticky broadcast: registering with a null receiver only reads the latest value.
        val sticky = runCatching {
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        }.getOrNull()

        val rawLevel = sticky?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = sticky?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val fromSticky = if (rawLevel >= 0 && scale > 0) {
            Math.round(rawLevel * 100f / scale)
        } else {
            null
        }
        val fromManager = runCatching {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager)
                .getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }.getOrNull()?.takeIf { it in 0..100 }

        val statusCode = sticky?.getIntExtra(
            BatteryManager.EXTRA_STATUS,
            BatteryManager.BATTERY_STATUS_UNKNOWN,
        ) ?: BatteryManager.BATTERY_STATUS_UNKNOWN
        val pluggedCode = sticky?.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) ?: 0
        val temperatureTenths =
            sticky?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, Int.MIN_VALUE) ?: Int.MIN_VALUE
        val healthCode =
            sticky?.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN)
                ?: BatteryManager.BATTERY_HEALTH_UNKNOWN

        return BatteryStats(
            levelPercent = fromSticky ?: fromManager,
            charging = statusCode == BatteryManager.BATTERY_STATUS_CHARGING ||
                (statusCode == BatteryManager.BATTERY_STATUS_FULL && pluggedCode != 0),
            status = statusLabel(statusCode),
            powerSource = powerSourceLabel(pluggedCode),
            voltageMillivolts = sticky?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
                ?.takeIf { it > 0 },
            temperatureCelsius = if (temperatureTenths == Int.MIN_VALUE) null else temperatureTenths / 10f,
            technology = sticky?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)?.takeIf { it.isNotBlank() },
            health = healthLabel(healthCode),
        )
    }

    private fun statusLabel(code: Int): String = when (code) {
        BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
        BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
        BatteryManager.BATTERY_STATUS_FULL -> "Full"
        BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not charging"
        else -> "Unknown"
    }

    private fun powerSourceLabel(code: Int): String = when (code) {
        BatteryManager.BATTERY_PLUGGED_AC -> "AC charger"
        BatteryManager.BATTERY_PLUGGED_USB -> "USB"
        BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
        else -> "Not plugged in"
    }

    private fun healthLabel(code: Int): String? = when (code) {
        BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
        BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheating"
        BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over voltage"
        BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
        BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
        else -> null
    }
}

class AndroidStorageSource(private val context: Context) {
    fun read(): StorageStats {
        val dataDir = Environment.getDataDirectory()
        val stat = runCatching { StatFs(dataDir.absolutePath) }.getOrNull()
        val total = stat?.let { it.blockCountLong * it.blockSizeLong } ?: 0L
        val available = stat?.let { it.availableBlocksLong * it.blockSizeLong } ?: 0L
        val sourceDir = context.applicationInfo.sourceDir
        return StorageStats(
            path = dataDir.absolutePath,
            totalBytes = total,
            availableBytes = available,
            appBytes = sourceDir?.let { runCatching { File(it).length() }.getOrDefault(0L) } ?: 0L,
            cacheBytes = directorySize(context.cacheDir),
        )
    }

    private fun directorySize(dir: File): Long = runCatching {
        dir.walkTopDown().maxDepth(4).filter { it.isFile }.sumOf { it.length() }
    }.getOrDefault(0L)
}

class AndroidCpuSource {
    fun read(): CpuStats {
        val coreCount = Runtime.getRuntime().availableProcessors()
        val cores = (0 until coreCount).map { index ->
            CpuCore(
                index = index,
                maxKHz = readLong("/sys/devices/system/cpu/cpu$index/cpufreq/cpuinfo_max_freq"),
                currentKHz = readLong("/sys/devices/system/cpu/cpu$index/cpufreq/scaling_cur_freq"),
            )
        }
        return CpuStats(
            coreCount = coreCount,
            supportedAbis = Build.SUPPORTED_ABIS?.toList().orEmpty(),
            hardware = Build.HARDWARE.orEmpty(),
            cores = cores,
        )
    }

    private fun readLong(path: String): Long? = runCatching {
        File(path).readText().trim().toLongOrNull()
    }.getOrNull()?.takeIf { it > 0L }
}

class AndroidDeviceSource(private val context: Context) {
    fun read(): DeviceDetails = DeviceDetails(
        manufacturer = Build.MANUFACTURER.orEmpty(),
        brand = Build.BRAND.orEmpty(),
        model = Build.MODEL.orEmpty(),
        device = Build.DEVICE.orEmpty(),
        product = Build.PRODUCT.orEmpty(),
        androidRelease = Build.VERSION.RELEASE.orEmpty(),
        apiLevel = Build.VERSION.SDK_INT,
        buildId = Build.ID.orEmpty(),
        buildType = Build.TYPE.orEmpty(),
        securityPatch = Build.VERSION.SECURITY_PATCH.orEmpty(),
        kernelVersion = System.getProperty("os.version").orEmpty(),
        runtime = System.getProperty("java.vm.version").orEmpty(),
        fingerprint = Build.FINGERPRINT.orEmpty(),
        uptimeMillis = SystemClock.elapsedRealtime(),
    )
}
