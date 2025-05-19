package com.glowstudio.android.blindsjn.feature.foodcost.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.foodcost.model.IngredientRequest
import com.glowstudio.android.blindsjn.feature.foodcost.repository.IngredientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IngredientViewModel : ViewModel() {
    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    fun registerIngredient(name: String, grams: Double, price: Int) {
        viewModelScope.launch {
            try {
                val request = IngredientRequest(name, grams, price)
                val response = IngredientRepository.registerIngredient(request)
                if (response.isSuccessful) {
                    _registerResult.value = response.body()?.message ?: "등록 성공"
                } else {
                    _registerResult.value = "등록 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _registerResult.value = "에러: ${e.message}"
            }
        }
    }

    fun clearResult() {
        _registerResult.value = null
    }
} 