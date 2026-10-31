package com.droidpanel.app.data

import java.util.Locale
import kotlin.math.abs

private val BYTE_UNITS = listOf("B", "KB", "MB", "GB", "TB", "PB")

/** SI (1000-based) byte size, matching what Android Settings shows. */
fun formatBytes(bytes: Long): String {
    var scaled = abs(bytes).toDouble()
    var unit = 0
    while (scaled >= 1000.0 && unit < BYTE_UNITS.lastIndex) {
        scaled /= 1000.0
        unit++
    }
    val number = if (unit == 0 || scaled >= 100.0) {
        String.format(Locale.US, "%.0f", scaled)
    } else {
        String.format(Locale.US, "%.1f", scaled)
    }
    val sign = if (bytes < 0) "-" else ""
    return "$sign$number ${BYTE_UNITS[unit]}"
}

/** Fraction in 0..1 rendered as a whole percentage. */
fun formatPercent(fraction: Float): String =
    String.format(Locale.US, "%.0f%%", (fraction.coerceIn(0f, 1f) * 100f))

fun formatUptime(millis: Long): String {
    val totalMinutes = millis.coerceAtLeast(0L) / 60_000L
    val days = totalMinutes / (60L * 24L)
    val hours = (totalMinutes / 60L) % 24L
    val minutes = totalMinutes % 60L
    return when {
        days > 0L -> "${days}d ${hours}h"
        hours > 0L -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}

fun formatFrequencyKHz(khz: Long): String = when {
    khz <= 0L -> "unavailable"
    khz >= 1_000_000L -> String.format(Locale.US, "%.2f GHz", khz / 1_000_000.0)
    else -> String.format(Locale.US, "%.0f MHz", khz / 1000.0)
}

fun formatCelsius(celsius: Float): String = String.format(Locale.US, "%.1f °C", celsius)

fun formatVoltage(millivolts: Int): String = String.format(Locale.US, "%.2f V", millivolts / 1000.0)
