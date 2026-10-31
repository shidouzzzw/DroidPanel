package com.droidpanel.app.data

/** Internal data-partition capacity from StatFs, plus this app's APK and cache size. */
data class StorageStats(
    val path: String,
    val totalBytes: Long,
    val availableBytes: Long,
    val appBytes: Long,
    val cacheBytes: Long,
) {
    val usedBytes: Long
        get() = (totalBytes - availableBytes).coerceIn(0L, totalBytes.coerceAtLeast(0L))

    val usedFraction: Float
        get() = if (totalBytes <= 0L) 0f else (usedBytes.toDouble() / totalBytes).toFloat().coerceIn(0f, 1f)
}
