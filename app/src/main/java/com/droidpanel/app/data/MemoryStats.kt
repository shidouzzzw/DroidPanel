package com.droidpanel.app.data

/** System RAM and this app's heap, read from ActivityManager.MemoryInfo and Runtime. */
data class MemoryStats(
    val totalBytes: Long,
    val availableBytes: Long,
    val thresholdBytes: Long,
    val lowMemory: Boolean,
    val appHeapUsedBytes: Long,
    val appHeapMaxBytes: Long,
) {
    val usedBytes: Long
        get() = (totalBytes - availableBytes).coerceIn(0L, totalBytes.coerceAtLeast(0L))

    val usedFraction: Float
        get() = if (totalBytes <= 0L) 0f else (usedBytes.toDouble() / totalBytes).toFloat().coerceIn(0f, 1f)

    val appHeapFraction: Float
        get() = if (appHeapMaxBytes <= 0L) 0f else (appHeapUsedBytes.toDouble() / appHeapMaxBytes).toFloat().coerceIn(0f, 1f)
}
