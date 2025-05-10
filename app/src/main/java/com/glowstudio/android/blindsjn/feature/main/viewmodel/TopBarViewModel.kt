package com.glowstudio.android.blindsjn.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 상단바 상태 데이터 클래스
 * title: 상단바에 표시할 제목
 * showBackButton: 뒤로가기 버튼 표시 여부
 * showSearchButton: 검색 버튼 표시 여부
 */
data class TopBarState(
    val title: String,
    val showBackButton: Boolean,
    val showSearchButton: Boolean
)

/**
 * 상단바 상태를 관리하는 ViewModel
 */
class TopBarViewModel : ViewModel() {
    private val _topBarState = MutableStateFlow(TopBarState("홈 화면", false, false))
    val topBarState = _topBarState.asStateFlow()

    fun updateState(newState: TopBarState) {
        _topBarState.value = newState
    }
}
