package com.droidpanel.app.data

/** Build-level device identity, all from public android.os.Build fields and system properties. */
data class DeviceDetails(
    val manufacturer: String,
    val brand: String,
    val model: String,
    val device: String,
    val product: String,
    val androidRelease: String,
    val apiLevel: Int,
    val buildId: String,
    val buildType: String,
    val securityPatch: String,
    val kernelVersion: String,
    val runtime: String,
    val fingerprint: String,
    val uptimeMillis: Long,
)
