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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.droidpanel.app.R
import com.droidpanel.app.model.DiagModule
import com.droidpanel.app.ui.components.DpChip
import com.droidpanel.app.ui.components.DpIcon
import com.droidpanel.app.ui.components.DpLabel
import com.droidpanel.app.ui.components.DpPanel
import com.droidpanel.app.ui.theme.DpTextPrimary
import com.droidpanel.app.ui.theme.DpTextSecondary

/** Phase 1 detail shell: honest "not implemented yet" state, no fake measurements. */
@Composable
fun ModuleDetailScreen(module: DiagModule, onBack: () -> Unit) {
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
        DpPanel(Modifier.fillMaxWidth()) {
            DpLabel("Status")
            Spacer(Modifier.height(10.dp))
            Text("Not implemented yet", style = MaterialTheme.typography.titleMedium, color = DpTextPrimary)
            Spacer(Modifier.height(4.dp))
            Text(
                "This module has no data source yet. No sample values are shown.",
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
}
