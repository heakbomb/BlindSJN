package com.glowstudio.android.blindsjn.feature.board.viewmodel

import androidx.lifecycle.ViewModel
import com.glowstudio.android.blindsjn.feature.board.model.BoardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WritePostViewModel(
    private val boardViewModel: BoardViewModel
) : ViewModel() {
    val categories: StateFlow<List<BoardCategory>> = boardViewModel.boardCategories

    private val _selectedCategory = MutableStateFlow(boardViewModel.boardCategories.value.first())
    val selectedCategory: StateFlow<BoardCategory> = _selectedCategory.asStateFlow()

    fun selectCategory(category: BoardCategory) {
        _selectedCategory.value = category
    }
} 