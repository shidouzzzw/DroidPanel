package com.droidpanel.app.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidpanel.app.R
import com.droidpanel.app.data.SystemSnapshot
import com.droidpanel.app.data.formatBytes
import com.droidpanel.app.data.formatPercent
import com.droidpanel.app.model.DiagModule
import com.droidpanel.app.ui.components.DpChip
import com.droidpanel.app.ui.components.DpIcon
import com.droidpanel.app.ui.components.DpLabel
import com.droidpanel.app.ui.components.DpMeter
import com.droidpanel.app.ui.components.DpPanel
import com.droidpanel.app.ui.stats.SystemStatsViewModel
import com.droidpanel.app.ui.theme.DpTextPrimary
import com.droidpanel.app.ui.theme.DpTextSecondary

@Composable
fun DashboardScreen(
    onOpenModule: (DiagModule) -> Unit,
    viewModel: SystemStatsViewModel = viewModel(),
) {
    val snapshot by viewModel.snapshot.collectAsStateWithLifecycle()
    val insets = WindowInsets.systemBars.union(WindowInsets.displayCutout).asPaddingValues()
    val dir = LocalLayoutDirection.current
    val padding = PaddingValues(
        start = insets.calculateStartPadding(dir) + 16.dp,
        end = insets.calculateEndPadding(dir) + 16.dp,
        top = insets.calculateTopPadding() + 24.dp,
        bottom = insets.calculateBottomPadding() + 24.dp,
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = padding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                DpLabel("System Core")
                Spacer(Modifier.height(4.dp))
                Text("DroidPanel", style = MaterialTheme.typography.headlineLarge, color = DpTextPrimary)
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            val current = snapshot
            DpPanel(Modifier.fillMaxWidth()) {
                DpLabel("Device")
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DpIcon(R.drawable.ic_device)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            current?.summary?.displayName ?: "Reading…",
                            style = MaterialTheme.typography.titleMedium,
                            color = DpTextPrimary,
                        )
                        Text(
                            if (current == null) {
                                "Collecting device state"
                            } else {
                                "Android ${current.summary.androidRelease} · API ${current.summary.apiLevel}"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = DpTextSecondary,
                        )
                    }
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            val current = snapshot
            DpPanel(Modifier.fillMaxWidth()) {
                DpLabel("Live system")
                Spacer(Modifier.height(12.dp))
                if (current == null) {
                    Text(
                        "Reading sensors…",
                        style = MaterialTheme.typography.bodySmall,
                        color = DpTextSecondary,
                    )
                } else {
                    LiveSystemRows(current)
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) { DpLabel("Modules") }
        items(DiagModule.entries, key = { it.id }) { module ->
            DpPanel(Modifier.fillMaxWidth(), onClick = { onOpenModule(module) }) {
                DpIcon(module.icon)
                Spacer(Modifier.height(14.dp))
                Text(module.title, style = MaterialTheme.typography.titleMedium, color = DpTextPrimary)
                Text(module.summary, style = MaterialTheme.typography.bodySmall, color = DpTextSecondary)
                Spacer(Modifier.height(12.dp))
                DpChip(if (module.live) "Live data" else "Phase ${module.phase}")
            }
        }
    }
}

@Composable
private fun LiveSystemRows(snapshot: SystemSnapshot) {
    val memory = snapshot.memory
    val battery = snapshot.battery
    val storage = snapshot.storage

    Column {
        LiveRow(
            name = "Memory",
            value = "${formatPercent(memory.usedFraction)} · ${formatBytes(memory.usedBytes)} / ${formatBytes(memory.totalBytes)}",
            fraction = memory.usedFraction,
        )
        Spacer(Modifier.height(12.dp))
        LiveRow(
            name = "Battery",
            value = "${battery.levelPercent?.let { "$it%" } ?: "—"} · ${battery.status}",
            fraction = battery.levelFraction,
        )
        Spacer(Modifier.height(12.dp))
        LiveRow(
            name = "Storage",
            value = "${formatPercent(storage.usedFraction)} · ${formatBytes(storage.availableBytes)} free",
            fraction = storage.usedFraction,
        )
    }
}

@Composable
private fun LiveRow(name: String, value: String, fraction: Float) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DpLabel(name)
            Text(value, style = MaterialTheme.typography.bodySmall, color = DpTextPrimary)
        }
        Spacer(Modifier.height(6.dp))
        DpMeter(fraction)
    }
}
