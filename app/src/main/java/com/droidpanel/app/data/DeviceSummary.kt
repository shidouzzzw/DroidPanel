package com.droidpanel.app.data

/** Basic identity of the device, read from public android.os.Build fields. */
data class DeviceSummary(
    val manufacturer: String,
    val model: String,
    val androidRelease: String,
    val apiLevel: Int,
) {
    /** Human-readable name without repeating the manufacturer when the model already contains it. */
    val displayName: String
        get() {
            val maker = manufacturer.trim().replaceFirstChar { it.uppercase() }
            val device = model.trim()
            return when {
                device.isEmpty() -> maker
                maker.isEmpty() -> device
                device.startsWith(maker, ignoreCase = true) -> device
                else -> "$maker $device"
            }
        }
}
