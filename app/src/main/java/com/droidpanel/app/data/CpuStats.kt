package com.droidpanel.app.data

/** One physical CPU core as exposed by the kernel, or an entry with null frequencies. */
data class CpuCore(
    val index: Int,
    val maxKHz: Long?,
    val currentKHz: Long?,
)

/** Cores and ABI data. Utilization is intentionally absent: it is not measurable from apps. */
data class CpuStats(
    val coreCount: Int,
    val supportedAbis: List<String>,
    val hardware: String,
    val cores: List<CpuCore>,
)
