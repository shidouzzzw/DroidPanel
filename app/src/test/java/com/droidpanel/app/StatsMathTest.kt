package com.droidpanel.app

import com.droidpanel.app.data.BatteryStats
import com.droidpanel.app.data.MemoryStats
import com.droidpanel.app.data.StorageStats
import org.junit.Assert.assertEquals
import org.junit.Test

class StatsMathTest {
    private fun memory(total: Long, available: Long) = MemoryStats(
        totalBytes = total,
        availableBytes = available,
        thresholdBytes = 0,
        lowMemory = false,
        appHeapUsedBytes = 10,
        appHeapMaxBytes = 100,
    )

    @Test fun usedBytesIsTotalMinusAvailable() {
        assertEquals(750L, memory(total = 1_000, available = 250).usedBytes)
    }

    @Test fun usedFractionNeverExceedsBounds() {
        assertEquals(0.75f, memory(total = 1_000, available = 250).usedFraction, 0.0001f)
        assertEquals(1f, memory(total = 1_000, available = -500).usedFraction, 0.0001f)
        assertEquals(0f, memory(total = 1_000, available = 5_000).usedFraction, 0.0001f)
    }

    @Test fun zeroTotalGivesZeroFraction() {
        assertEquals(0f, memory(total = 0, available = 0).usedFraction, 0.0001f)
    }

    @Test fun storageFractionMatchesUsedBytes() {
        val storage = StorageStats("", totalBytes = 2_000, availableBytes = 500, appBytes = 0, cacheBytes = 0)
        assertEquals(0.75f, storage.usedFraction, 0.0001f)
        assertEquals(0f, StorageStats("", 0, 0, 0, 0).usedFraction, 0.0001f)
    }

    @Test fun batteryLevelFractionHandlesMissingLevel() {
        val missing = BatteryStats(null, false, "Unknown", "Not plugged in", null, null, null, null)
        assertEquals(0f, missing.levelFraction, 0.0001f)
        val full = missing.copy(levelPercent = 150)
        assertEquals(1f, full.levelFraction, 0.0001f)
        val quarter = missing.copy(levelPercent = 25)
        assertEquals(0.25f, quarter.levelFraction, 0.0001f)
    }
}
