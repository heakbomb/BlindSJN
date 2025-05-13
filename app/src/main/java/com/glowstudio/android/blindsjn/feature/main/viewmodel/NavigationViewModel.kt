package com.glowstudio.android.blindsjn.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.main.model.NavigationState
import com.glowstudio.android.blindsjn.feature.main.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationViewModel : ViewModel() {
    private val _navigationState = MutableStateFlow(NavigationState())
    val navigationState: StateFlow<NavigationState> = _navigationState.asStateFlow()

    /**
     * 현재 네비게이션 상태의 경로를 주어진 값으로 비동기적으로 업데이트합니다.
     *
     * @param route 새로운 현재 경로로 설정할 문자열
     */
    fun updateCurrentRoute(route: String) {
        viewModelScope.launch {
            _navigationState.value = _navigationState.value.copy(currentRoute = route)
        }
    }

    /**
     * 주어진 경로로 네비게이션 상태를 업데이트합니다.
     *
     * @param route 이동할 화면의 경로를 나타내는 문자열입니다.
     */
    fun navigateToScreen(route: String) {
        viewModelScope.launch {
            _navigationState.value = _navigationState.value.copy(currentRoute = route)
        }
    }
} 