package com.droidpanel.app

import com.droidpanel.app.model.DiagModule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DiagModuleTest {
    @Test fun idsAreUnique() {
        assertEquals(DiagModule.entries.size, DiagModule.entries.map { it.id }.toSet().size)
    }
    @Test fun fromIdResolvesAndRejectsUnknown() {
        assertEquals(DiagModule.MEMORY, DiagModule.fromId("memory"))
        assertNull(DiagModule.fromId("nope"))
        assertNull(DiagModule.fromId(null))
    }
    @Test fun coreModulesReadLiveData() {
        listOf(DiagModule.MEMORY, DiagModule.BATTERY, DiagModule.STORAGE, DiagModule.CPU, DiagModule.DEVICE)
            .forEach { assertTrue("${it.id} should be live", it.live) }
        listOf(DiagModule.GPU, DiagModule.NETWORK, DiagModule.APPS).forEach {
            assertFalse("${it.id} has no data source yet", it.live)
        }
    }

    @Test fun everyModuleHasPlannedScopeAndValidPhase() {
        DiagModule.entries.forEach {
            assertTrue(it.planned.isNotEmpty())
            assertTrue(it.phase in 2..7)
        }
    }
}
