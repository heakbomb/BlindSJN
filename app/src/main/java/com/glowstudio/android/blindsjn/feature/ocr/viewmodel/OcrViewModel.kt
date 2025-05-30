package com.glowstudio.android.blindsjn.feature.ocr.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.ocr.repository.OcrRepository
import com.glowstudio.android.blindsjn.feature.ocr.repository.OcrRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

data class OcrUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class OcrViewModel : ViewModel() {
    private val repository: OcrRepository = OcrRepositoryImpl()
    private val _uiState = MutableStateFlow(OcrUiState())
    val uiState: StateFlow<OcrUiState> = _uiState.asStateFlow()

    fun processImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                // Convert URI to Bitmap
                val bitmap = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                } ?: throw IOException("Failed to load image")

                // Process image with OCR
                repository.processImage(bitmap).collect { result ->
                    // Handle OCR result
                    // TODO: Implement OCR result handling
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to process image"
                )
            }
        }
    }
} 