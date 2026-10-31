package com.droidpanel.app.data

import android.os.Build

/** Data source backed by public [Build] constants. No permissions needed. */
class AndroidBuildSource {
    fun read(): DeviceSummary = DeviceSummary(
        manufacturer = Build.MANUFACTURER.orEmpty(),
        model = Build.MODEL.orEmpty(),
        androidRelease = Build.VERSION.RELEASE.orEmpty(),
        apiLevel = Build.VERSION.SDK_INT,
    )
}
