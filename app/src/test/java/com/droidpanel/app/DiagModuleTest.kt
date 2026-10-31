package com.droidpanel.app

import com.droidpanel.app.model.DiagModule
import org.junit.Assert.assertEquals
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
    @Test fun everyModuleHasPlannedScopeAndValidPhase() {
        DiagModule.entries.forEach {
            assertTrue(it.planned.isNotEmpty())
            assertTrue(it.phase in 2..7)
        }
    }
}
