package com.droidpanel.app

import com.droidpanel.app.data.DeviceSummary
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceSummaryTest {
    @Test fun prependsManufacturerWhenMissing() {
        assertEquals("Samsung SM-S918B", DeviceSummary("samsung", "SM-S918B", "14", 34).displayName)
    }
    @Test fun doesNotDuplicateManufacturer() {
        assertEquals("Google Pixel 8", DeviceSummary("Google", "Google Pixel 8", "15", 35).displayName)
    }
    @Test fun handlesEmptyModel() {
        assertEquals("Xiaomi", DeviceSummary("xiaomi", "", "14", 34).displayName)
    }
    @Test fun handlesEmptyManufacturer() {
        assertEquals("Pixel", DeviceSummary("", "Pixel", "14", 34).displayName)
    }
}
