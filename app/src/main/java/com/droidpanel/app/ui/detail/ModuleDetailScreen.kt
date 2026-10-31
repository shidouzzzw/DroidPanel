package com.droidpanel.app.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidpanel.app.R
import com.droidpanel.app.data.BatteryStats
import com.droidpanel.app.data.CpuStats
import com.droidpanel.app.data.DeviceDetails
import com.droidpanel.app.data.MemoryStats
import com.droidpanel.app.data.StorageStats
import com.droidpanel.app.data.formatBytes
import com.droidpanel.app.data.formatCelsius
import com.droidpanel.app.data.formatFrequencyKHz
import com.droidpanel.app.data.formatPercent
import com.droidpanel.app.data.formatUptime
import com.droidpanel.app.data.formatVoltage
import com.droidpanel.app.model.DiagModule
import com.droidpanel.app.ui.components.DpChip
import com.droidpanel.app.ui.components.DpIcon
import com.droidpanel.app.ui.components.DpLabel
import com.droidpanel.app.ui.components.DpMeter
import com.droidpanel.app.ui.components.DpPanel
import com.droidpanel.app.ui.components.DpRow
import com.droidpanel.app.ui.stats.SystemStatsViewModel
import com.droidpanel.app.ui.theme.DpTextPrimary
import com.droidpanel.app.ui.theme.DpTextSecondary

/**
 * Module detail. Live modules show measurements read from the device on this frame; the remaining
 * modules keep an explicit "no data source yet" state instead of invented values.
 */
@Composable
fun ModuleDetailScreen(
    module: DiagModule,
    onBack: () -> Unit,
    viewModel: SystemStatsViewModel = viewModel(),
) {
    val snapshot by viewModel.snapshot.collectAsStateWithLifecycle()
    BackHandler(onBack = onBack)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.union(WindowInsets.displayCutout))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(role = Role.Button, onClick = onBack)
                .padding(vertical = 8.dp),
        ) {
            DpIcon(R.drawable.ic_back)
            Spacer(Modifier.width(8.dp))
            DpLabel("Back")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            DpIcon(module.icon)
            Spacer(Modifier.width(12.dp))
            Text(module.title, style = MaterialTheme.typography.headlineLarge, color = DpTextPrimary)
        }
        if (module.live && snapshot == null) {
            DpPanel(Modifier.fillMaxWidth()) {
                DpLabel("Status")
                Spacer(Modifier.height(10.dp))
                Text("Reading device…", style = MaterialTheme.typography.titleMedium, color = DpTextPrimary)
            }
        } else {
            val current = snapshot
            when (module) {
                DiagModule.MEMORY -> if (current != null) MemorySection(current.memory) else Unit
                DiagModule.BATTERY -> if (current != null) BatterySection(current.battery) else Unit
                DiagModule.STORAGE -> if (current != null) StorageSection(current.storage) else Unit
                DiagModule.CPU -> if (current != null) CpuSection(current.cpu) else Unit
                DiagModule.DEVICE -> if (current != null) DeviceSection(current.device) else Unit
                else -> PendingSection(module)
            }
        }
    }
}

@Composable
private fun SourceNote(text: String) {
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Data source")
        Spacer(Modifier.height(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = DpTextSecondary)
    }
}

@Composable
private fun MemorySection(memory: MemoryStats) {
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("RAM usage")
        Spacer(Modifier.height(10.dp))
        Text(
            "${formatBytes(memory.usedBytes)} of ${formatBytes(memory.totalBytes)} in use",
            style = MaterialTheme.typography.titleMedium,
            color = DpTextPrimary,
        )
        Spacer(Modifier.height(10.dp))
        DpMeter(memory.usedFraction)
        Spacer(Modifier.height(8.dp))
        DpRow("Usage", formatPercent(memory.usedFraction))
        DpRow("Total", formatBytes(memory.totalBytes))
        DpRow("Available", formatBytes(memory.availableBytes))
        DpRow("Used", formatBytes(memory.usedBytes))
        DpRow("Low-memory threshold", formatBytes(memory.thresholdBytes))
        DpRow("Low memory state", if (memory.lowMemory) "Low" else "Normal")
        DpRow("App heap", "${formatBytes(memory.appHeapUsedBytes)} / ${formatBytes(memory.appHeapMaxBytes)}")
        Spacer(Modifier.height(8.dp))
        DpChip(if (memory.lowMemory) "Pressure high" else "Pressure normal")
    }
    SourceNote("Read live from ActivityManager.MemoryInfo; the app heap comes from Runtime.")
}

@Composable
private fun BatterySection(battery: BatteryStats) {
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Battery")
        Spacer(Modifier.height(10.dp))
        Text(
            battery.levelPercent?.let { "$it%" } ?: "Level not reported",
            style = MaterialTheme.typography.headlineLarge,
            color = DpTextPrimary,
        )
        Text(battery.status, style = MaterialTheme.typography.bodySmall, color = DpTextSecondary)
        Spacer(Modifier.height(10.dp))
        DpMeter(battery.levelFraction)
        Spacer(Modifier.height(8.dp))
        DpRow("Status", battery.status)
        DpRow("Power source", battery.powerSource)
        DpRow("Voltage", battery.voltageMillivolts?.let { formatVoltage(it) } ?: "unavailable")
        DpRow("Temperature", battery.temperatureCelsius?.let { formatCelsius(it) } ?: "unavailable")
        DpRow("Technology", battery.technology ?: "unavailable")
        DpRow("Health", battery.health ?: "not reported")
    }
    SourceNote("Read from the sticky ACTION_BATTERY_CHANGED broadcast; no permission is required.")
}

@Composable
private fun StorageSection(storage: StorageStats) {
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Internal storage")
        Spacer(Modifier.height(10.dp))
        Text(
            "${formatBytes(storage.usedBytes)} used of ${formatBytes(storage.totalBytes)}",
            style = MaterialTheme.typography.titleMedium,
            color = DpTextPrimary,
        )
        Spacer(Modifier.height(10.dp))
        DpMeter(storage.usedFraction)
        Spacer(Modifier.height(8.dp))
        DpRow("Usage", formatPercent(storage.usedFraction))
        DpRow("Total", formatBytes(storage.totalBytes))
        DpRow("Free", formatBytes(storage.availableBytes))
        DpRow("Partition", storage.path)
    }
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("This app")
        Spacer(Modifier.height(8.dp))
        DpRow("APK size", formatBytes(storage.appBytes))
        DpRow("Cache", formatBytes(storage.cacheBytes))
    }
    SourceNote("Read with StatFs on the data partition. Apps cannot see other apps' storage on Android 11+.")
}

@Composable
private fun CpuSection(cpu: CpuStats) {
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Processor")
        Spacer(Modifier.height(8.dp))
        DpRow("Cores", cpu.coreCount.toString())
        DpRow("Hardware", cpu.hardware.ifBlank { "unavailable" })
        DpRow("ABIs", cpu.supportedAbis.joinToString(", ").ifBlank { "unavailable" })
    }
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Core frequencies")
        Spacer(Modifier.height(8.dp))
        cpu.cores.forEach { core ->
            DpRow(
                label = "Core ${core.index}",
                value = buildString {
                    append(core.currentKHz?.let { formatFrequencyKHz(it) } ?: "—")
                    core.maxKHz?.let { append(" / max ${formatFrequencyKHz(it)}") }
                },
            )
        }
    }
    SourceNote("Frequencies are read from /sys cpufreq entries where the kernel exposes them. Utilization is not shown because apps cannot measure it reliably.")
}

@Composable
private fun DeviceSection(device: DeviceDetails) {
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Build")
        Spacer(Modifier.height(8.dp))
        DpRow("Manufacturer", device.manufacturer.ifBlank { "unknown" })
        DpRow("Brand", device.brand.ifBlank { "unknown" })
        DpRow("Model", device.model.ifBlank { "unknown" })
        DpRow("Device", device.device.ifBlank { "unknown" })
        DpRow("Product", device.product.ifBlank { "unknown" })
        DpRow("Android", device.androidRelease.ifBlank { "unknown" })
        DpRow("API level", device.apiLevel.toString())
        DpRow("Build ID", device.buildId.ifBlank { "unknown" })
        DpRow("Build type", device.buildType.ifBlank { "unknown" })
        DpRow("Security patch", device.securityPatch.ifBlank { "unknown" })
    }
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Runtime")
        Spacer(Modifier.height(8.dp))
        DpRow("Kernel", device.kernelVersion.ifBlank { "unavailable" })
        DpRow("ART runtime", device.runtime.ifBlank { "unavailable" })
        DpRow("Uptime", formatUptime(device.uptimeMillis))
    }
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Fingerprint")
        Spacer(Modifier.height(8.dp))
        Text(
            device.fingerprint.ifBlank { "unavailable" },
            style = MaterialTheme.typography.bodySmall,
            color = DpTextSecondary,
        )
    }
    SourceNote("Read from android.os.Build and public system properties.")
}

@Composable
private fun PendingSection(module: DiagModule) {
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Status")
        Spacer(Modifier.height(10.dp))
        Text("No data source yet", style = MaterialTheme.typography.titleMedium, color = DpTextPrimary)
        Spacer(Modifier.height(4.dp))
        Text(
            "This module does not read the device yet, so no values are shown instead of samples.",
            style = MaterialTheme.typography.bodySmall,
            color = DpTextSecondary,
        )
        Spacer(Modifier.height(12.dp))
        DpChip("Planned: Phase ${module.phase}")
    }
    DpPanel(Modifier.fillMaxWidth()) {
        DpLabel("Planned scope")
        Spacer(Modifier.height(10.dp))
        module.planned.forEach { item ->
            Text(
                "— $item",
                style = MaterialTheme.typography.bodyMedium,
                color = DpTextSecondary,
                modifier = Modifier.padding(vertical = 2.dp),
            )
        }
    }
}
