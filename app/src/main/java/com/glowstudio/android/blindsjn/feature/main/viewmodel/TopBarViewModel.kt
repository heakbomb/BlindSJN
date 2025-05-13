package com.glowstudio.android.blindsjn.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TopBarType { MAIN, DETAIL }

/**
 * 상단바 상태 데이터 클래스
 * title: 상단바에 표시할 제목
 * showBackButton: 뒤로가기 버튼 표시 여부
 * showSearchButton: 검색 버튼 표시 여부
 */
data class TopBarState(
    val type: TopBarType = TopBarType.MAIN,
    val title: String = "",
    val showBackButton: Boolean = false,
    val showSearchButton: Boolean = false,
    val showMoreButton: Boolean = false
)

/**
 * 상단바 상태를 관리하는 ViewModel
 */
class TopBarViewModel : ViewModel() {
    private val _topBarState = MutableStateFlow(TopBarState())
    val topBarState = _topBarState.asStateFlow()

    /**
     * 상단 바 상태를 기본 메인 바로 초기화합니다.
     *
     * 타입을 MAIN으로 설정하고, 타이틀과 버튼 표시 여부는 기본값으로 재설정합니다.
     */
    fun setMainBar() {
        _topBarState.value = TopBarState(type = TopBarType.MAIN)
    }

    /**
     * 상단 바를 상세 화면 모드로 설정하고, 지정한 제목과 함께 뒤로가기, 검색, 더보기 버튼을 모두 표시합니다.
     *
     * @param title 상단 바에 표시할 제목 텍스트
     */
    fun setDetailBar(title: String) {
        _topBarState.value = TopBarState(
            type = TopBarType.DETAIL,
            title = title,
            showBackButton = true,
            showSearchButton = true,
            showMoreButton = true
        )
    }
}
