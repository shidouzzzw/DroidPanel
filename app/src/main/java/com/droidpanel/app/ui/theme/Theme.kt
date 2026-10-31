package com.droidpanel.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DpColorScheme = darkColorScheme(
    primary = DpTextPrimary,
    onPrimary = DpBackground,
    background = DpBackground,
    onBackground = DpTextPrimary,
    surface = DpSurface,
    onSurface = DpTextPrimary,
    surfaceVariant = DpElevated,
    onSurfaceVariant = DpTextSecondary,
    outline = DpBorder,
    outlineVariant = DpBorder,
)

@Composable
fun DroidPanelTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DpColorScheme, typography = DpTypography, content = content)
}
