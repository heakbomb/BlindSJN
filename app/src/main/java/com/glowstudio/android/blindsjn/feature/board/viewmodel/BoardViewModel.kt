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
            BoardCategory("🏢", "업종게시판", "industry"),
            BoardCategory("💬", "자유게시판", "free"),
            BoardCategory("❓", "질문게시판", "question"),
            BoardCategory("🔥", "인기게시판", "hot")
        )
    }
}