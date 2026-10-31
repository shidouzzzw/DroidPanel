package com.droidpanel.app.ui.dashboard

import androidx.lifecycle.ViewModel
import com.droidpanel.app.data.AndroidBuildSource
import com.droidpanel.app.data.DeviceRepository
import com.droidpanel.app.data.DeviceSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DashboardUiState(val device: DeviceSummary)

class DashboardViewModel(
    repository: DeviceRepository = DeviceRepository(AndroidBuildSource()),
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardUiState(repository.summary()))
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()
}
