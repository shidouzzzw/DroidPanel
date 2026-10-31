package com.droidpanel.app.model

import androidx.annotation.DrawableRes
import com.droidpanel.app.R

/**
 * Diagnostics modules shown on the dashboard.
 * [live] is true when the module reads real data from the device; [phase] is the roadmap phase in
 * which the remaining modules get theirs.
 */
enum class DiagModule(
    val id: String,
    val title: String,
    val summary: String,
    @DrawableRes val icon: Int,
    val phase: Int,
    val planned: List<String>,
    val live: Boolean = false,
) {
    MEMORY("memory", "Memory", "RAM totals and pressure", R.drawable.ic_memory, 2,
        listOf("Total, available and used RAM", "Usage percentage", "Memory pressure when supported", "Particle visualization of utilization", "Timestamped history"),
        live = true),
    BATTERY("battery", "Battery & Thermal", "Charge, power, temperature", R.drawable.ic_battery, 2,
        listOf("Level, voltage, charging state and source", "Battery temperature", "Health only when reliably reported", "Android thermal status"),
        live = true),
    STORAGE("storage", "Storage", "Internal capacity and usage", R.drawable.ic_storage, 2,
        listOf("Total, available and used internal storage", "Accessible filesystem details", "App storage and cache where permitted"),
        live = true),
    CPU("cpu", "CPU", "Cores, ABI, frequencies", R.drawable.ic_cpu, 4,
        listOf("Architecture and core count", "Frequency ranges when accessible", "Live utilization only if reliably measurable", "Utilization history"),
        live = true),
    GPU("gpu", "GPU & Graphics", "Renderer and API support", R.drawable.ic_gpu, 4,
        listOf("Renderer and vendor when available", "OpenGL ES and Vulkan capabilities", "No fabricated utilization, clock or temperature")),
    DISPLAY("display", "Display", "Resolution, density, refresh", R.drawable.ic_display, 4,
        listOf("Resolution and density", "Refresh rate (tracks changes)", "HDR and display capabilities")),
    SENSORS("sensors", "Sensors", "Inventory and live inspection", R.drawable.ic_sensors, 4,
        listOf("Sensor list with vendor, type and resolution", "Opt-in live readings", "Listeners registered only while needed")),
    NETWORK("network", "Network", "Connectivity and link info", R.drawable.ic_network, 5,
        listOf("Connection type and state", "Wi-Fi and link information", "Local IP where accessible", "No internet speed claims without a real test")),
    APPS("apps", "Applications", "Visible installed apps", R.drawable.ic_apps, 5,
        listOf("Name, package, version", "Size where accessible", "Package visibility limits explained")),
    DEVICE("device", "Android & Device", "Build, API, kernel", R.drawable.ic_device, 5,
        listOf("Manufacturer, model, release, API level", "Build info and security patch", "Kernel and runtime information"),
        live = true),
    CAMERA_AUDIO("camera_audio", "Camera & Audio", "Capabilities only", R.drawable.ic_camera, 5,
        listOf("Camera characteristics and output sizes", "Audio devices and capabilities", "Never activates camera or microphone without user action")),
    SECURITY_PERF("security_perf", "Security & Performance", "Patch level, capabilities", R.drawable.ic_security, 6,
        listOf("Security patch and device security capabilities", "Biometric/lock availability via supported APIs", "Performance samples and history"));

    companion object {
        fun fromId(id: String?): DiagModule? = entries.firstOrNull { it.id == id }
    }
}
