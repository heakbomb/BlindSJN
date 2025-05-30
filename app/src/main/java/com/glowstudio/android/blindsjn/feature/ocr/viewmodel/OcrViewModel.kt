package com.glowstudio.android.blindsjn.feature.ocr.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.ocr.repository.OcrRepository
import com.glowstudio.android.blindsjn.feature.ocr.repository.OcrRepositoryImpl
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OcrUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val result: OcrResult? = null
)

class OcrViewModel : ViewModel() {
    private val repository: OcrRepository = OcrRepositoryImpl()
    private val _uiState = MutableStateFlow(OcrUiState())
    val uiState: StateFlow<OcrUiState> = _uiState.asStateFlow()

    fun processImage(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null, result = null)
                
                repository.processImage(bitmap).collect { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = result
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to process image",
                    result = null
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
} 