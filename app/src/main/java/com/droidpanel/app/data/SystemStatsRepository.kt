package com.droidpanel.app.data

import android.content.Context

/** One consistent read of every live module. */
data class SystemSnapshot(
    val summary: DeviceSummary,
    val memory: MemoryStats,
    val battery: BatteryStats,
    val storage: StorageStats,
    val cpu: CpuStats,
    val device: DeviceDetails,
    val readAtMillis: Long,
)

class SystemStatsRepository(private val context: Context) {
    private val buildSource = AndroidBuildSource()
    private val memorySource = AndroidMemorySource(context)
    private val batterySource = AndroidBatterySource(context)
    private val storageSource = AndroidStorageSource(context)
    private val cpuSource = AndroidCpuSource()
    private val deviceSource = AndroidDeviceSource(context)

    fun read(): SystemSnapshot = SystemSnapshot(
        summary = buildSource.read(),
        memory = memorySource.read(),
        battery = batterySource.read(),
        storage = storageSource.read(),
        cpu = cpuSource.read(),
        device = deviceSource.read(),
        readAtMillis = System.currentTimeMillis(),
    )
}
