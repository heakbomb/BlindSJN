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
     * 선택 가능한 태그 목록에 포함된 경우, 해당 태그의 선택 상태를 토글합니다.
     *
     * 이미 선택된 태그라면 선택 해제하고, 선택되지 않은 태그라면 선택합니다.
     * 선택 불가능한 태그에 대해서는 아무 동작도 하지 않습니다.
     *
     * @param tag 토글할 태그의 이름
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
     * 선택된 태그 목록을 비워 선택 상태를 해제합니다.
     */
    fun clearSelection() {
        _selectedTags.value = emptySet()
    }

    /**
     * 전체 태그 목록과 선택 가능한 태그 목록을 갱신하고, 선택된 태그를 초기화합니다.
     *
     * @param tags 새로운 전체 태그 목록
     * @param enabledTags 선택 가능 태그 목록
     */
    fun setTags(tags: List<String>, enabledTags: List<String>) {
        _tags.value = tags
        _enabledTags.value = enabledTags
        _selectedTags.value = emptySet()
    }
} 