package com.glowstudio.android.blindsjn.feature.ocr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OcrResultUiState(
    val items: List<OcrItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class OcrResultViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OcrResultUiState())
    val uiState: StateFlow<OcrResultUiState> = _uiState.asStateFlow()

    fun updateItems(items: List<OcrItem>) {
        _uiState.update { it.copy(items = items) }
    }

    fun updateItem(index: Int, item: OcrItem) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.toMutableList()
            updatedItems[index] = item
            currentState.copy(items = updatedItems)
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun setError(error: String?) {
        _uiState.update { it.copy(error = error) }
    }
} 