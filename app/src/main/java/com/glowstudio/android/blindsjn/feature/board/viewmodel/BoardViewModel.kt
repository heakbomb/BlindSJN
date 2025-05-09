package com.glowstudio.android.blindsjn.feature.board.viewmodel

import androidx.lifecycle.ViewModel
import com.glowstudio.android.blindsjn.feature.board.model.BoardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BoardViewModel : ViewModel() {
    private val _boardCategories = MutableStateFlow<List<BoardCategory>>(emptyList())
    val boardCategories: StateFlow<List<BoardCategory>> = _boardCategories

    init {
        loadBoardCategories()
    }

    private fun loadBoardCategories() {
        _boardCategories.value = listOf(
            BoardCategory("🍴", "음식점 및 카페", "298개의 새 게시물"),
            BoardCategory("🛍️", "쇼핑 및 리테일", "128개의 새 게시물"),
            BoardCategory("💊", "건강 및 의료", "57개의 새 게시물"),
            BoardCategory("🏨", "숙박 및 여행", "298개의 새 게시물"),
            BoardCategory("📚", "교육 및 학습", "36개의 새 게시물"),
            BoardCategory("🎮", "여가 및 오락", "98개의 새 게시물"),
            BoardCategory("💰", "금융 및 공공기관", "20개의 새 게시물")
        )
    }
}