package com.glowstudio.android.blindsjn.feature.board

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BoardCategory(
    val emoji: String,
    val title: String,
    val postCount: String
)

class BoardViewModel : ViewModel() {
    private val _boardCategories = MutableStateFlow(
        listOf(
            BoardCategory("🍴", "음식점 및 카페", "298개의 새 게시물"),
            BoardCategory("🛍️", "쇼핑 및 리테일", "128개의 새 게시물"),
            BoardCategory("💊", "건강 및 의료", "57개의 새 게시물"),
            BoardCategory("🏨", "숙박 및 여행", "298개의 새 게시물"),
            BoardCategory("📚", "교육 및 학습", "36개의 새 게시물"),
            BoardCategory("🎮", "여가 및 오락", "98개의 새 게시물"),
            BoardCategory("💰", "금융 및 공공기관", "20개의 새 게시물")
        )
    )
    val boardCategories: StateFlow<List<BoardCategory>> = _boardCategories
}