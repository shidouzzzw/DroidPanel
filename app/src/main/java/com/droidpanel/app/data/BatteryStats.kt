package com.droidpanel.app.data

/** Live battery reading from the ACTION_BATTERY_CHANGED broadcast. Absent fields stay null. */
data class BatteryStats(
    val levelPercent: Int?,
    val charging: Boolean,
    val status: String,
    val powerSource: String,
    val voltageMillivolts: Int?,
    val temperatureCelsius: Float?,
    val technology: String?,
    val health: String?,
) {
    val levelFraction: Float
        get() = ((levelPercent ?: 0).coerceIn(0, 100)) / 100f
}
