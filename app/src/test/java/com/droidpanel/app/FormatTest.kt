package com.droidpanel.app

import com.droidpanel.app.data.formatBytes
import com.droidpanel.app.data.formatCelsius
import com.droidpanel.app.data.formatFrequencyKHz
import com.droidpanel.app.data.formatPercent
import com.droidpanel.app.data.formatUptime
import com.droidpanel.app.data.formatVoltage
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatTest {
    @Test fun formatsBytesAcrossUnits() {
        assertEquals("0 B", formatBytes(0))
        assertEquals("512 B", formatBytes(512))
        assertEquals("1.0 KB", formatBytes(1_000))
        assertEquals("7.7 GB", formatBytes(7_700_000_000))
        assertEquals("13.0 GB", formatBytes(13_000_000_000))
    }

    @Test fun formatsPercentWithinBounds() {
        assertEquals("0%", formatPercent(-0.4f))
        assertEquals("62%", formatPercent(0.6234f))
        assertEquals("100%", formatPercent(3f))
    }

    @Test fun formatsUptime() {
        assertEquals("0m", formatUptime(59_000))
        assertEquals("1m", formatUptime(90_000))
        assertEquals("5h 7m", formatUptime(5 * 3_600_000L + 7 * 60_000L))
        assertEquals("2d 3h", formatUptime(2 * 86_400_000L + 3 * 3_600_000L))
    }

    @Test fun formatsFrequencies() {
        assertEquals("unavailable", formatFrequencyKHz(0))
        assertEquals("800 MHz", formatFrequencyKHz(800_000))
        assertEquals("2.84 GHz", formatFrequencyKHz(2_840_000))
    }

    @Test fun formatsBatteryUnits() {
        assertEquals("31.5 °C", formatCelsius(31.5f))
        assertEquals("4.21 V", formatVoltage(4210))
    }
}
