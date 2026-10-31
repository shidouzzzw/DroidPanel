package com.droidpanel.app.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.droidpanel.app.data.SystemSnapshot
import com.droidpanel.app.data.SystemStatsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/** Polls the live modules while a screen collects the flow, so nothing runs in the background. */
class SystemStatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SystemStatsRepository(application)

    val snapshot: StateFlow<SystemSnapshot?> = flow {
        while (true) {
            runCatching { repository.read() }.getOrNull()?.let { emit(it) }
            delay(REFRESH_INTERVAL_MS)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private companion object {
        const val REFRESH_INTERVAL_MS = 2_000L
    }
}
