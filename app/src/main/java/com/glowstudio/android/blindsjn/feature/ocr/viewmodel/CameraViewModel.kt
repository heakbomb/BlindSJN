package com.glowstudio.android.blindsjn.feature.ocr.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResult
import com.glowstudio.android.blindsjn.feature.ocr.repository.OcrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {
    private val ocrRepository = OcrRepository()
    
    private val _ocrResults = MutableStateFlow<List<OcrResult>>(emptyList())
    val ocrResults: StateFlow<List<OcrResult>> = _ocrResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    fun processImage(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val results = ocrRepository.analyzeReceipt(uri)
                results.fold(
                    onSuccess = { ocrResults ->
                        _ocrResults.value = ocrResults
                    },
                    onFailure = { e ->
                        _error.value = e.message ?: "영수증 분석 중 오류가 발생했습니다."
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "영수증 분석 중 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateOcrResult(index: Int, result: OcrResult) {
        val currentResults = _ocrResults.value.toMutableList()
        if (index in currentResults.indices) {
            currentResults[index] = result
            _ocrResults.value = currentResults
        }
    }

    fun saveOcrResults() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _saveSuccess.value = false

            try {
                ocrRepository.saveOcrResults(_ocrResults.value)
                    .fold(
                        onSuccess = {
                            _saveSuccess.value = true
                        },
                        onFailure = { e ->
                            _error.value = e.message ?: "결과 저장 중 오류가 발생했습니다."
                        }
                    )
            } catch (e: Exception) {
                _error.value = e.message ?: "결과 저장 중 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTotalAmount(): Int {
        return _ocrResults.value.sumOf { it.price * it.quantity }
    }

    fun clearResults() {
        _ocrResults.value = emptyList()
        _error.value = null
        _saveSuccess.value = false
    }
} 