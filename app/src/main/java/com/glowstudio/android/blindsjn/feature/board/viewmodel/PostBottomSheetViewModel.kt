package com.glowstudio.android.blindsjn.feature.board.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostBottomSheetViewModel : ViewModel() {
    // 전체 태그 목록
    private val _tags = MutableStateFlow<List<String>>(listOf("누구나", "재학생", "패션디자인", "비활성1", "비활성2"))
    val tags: StateFlow<List<String>> = _tags.asStateFlow()

    // 선택 가능한 태그 목록
    private val _enabledTags = MutableStateFlow<List<String>>(listOf("누구나", "재학생", "패션디자인"))
    val enabledTags: StateFlow<List<String>> = _enabledTags.asStateFlow()

    // 선택된 태그 목록
    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    val selectedTags: StateFlow<Set<String>> = _selectedTags.asStateFlow()

    /**
     * 주어진 태그의 선택 상태를 토글합니다.
     *
     * 태그가 활성화된 목록에 포함되어 있을 때만 선택 또는 선택 해제를 수행합니다.
     *
     * @param tag 토글할 태그 이름
     */
    fun toggleTag(tag: String) {
        if (!_enabledTags.value.contains(tag)) return
        _selectedTags.value = if (_selectedTags.value.contains(tag)) {
            _selectedTags.value - tag
        } else {
            _selectedTags.value + tag
        }
    }

    /**
     * 선택된 모든 태그를 초기화합니다.
     *
     * 현재 선택된 태그 목록을 비워 선택 상태를 해제합니다.
     */
    fun clearSelection() {
        _selectedTags.value = emptySet()
    }

    /**
     * 전체 태그 목록과 선택 가능한 태그 목록을 갱신하고, 선택된 태그를 초기화합니다.
     *
     * @param tags 새로운 전체 태그 목록
     * @param enabledTags 선택 가능하도록 활성화할 태그 목록
     */
    fun setTags(tags: List<String>, enabledTags: List<String>) {
        _tags.value = tags
        _enabledTags.value = enabledTags
        _selectedTags.value = emptySet()
    }
} 