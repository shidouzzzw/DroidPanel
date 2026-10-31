package com.droidpanel.app.data

class DeviceRepository(private val source: AndroidBuildSource) {
    fun summary(): DeviceSummary = source.read()
}
