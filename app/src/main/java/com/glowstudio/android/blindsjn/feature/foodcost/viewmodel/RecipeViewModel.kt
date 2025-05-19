package com.glowstudio.android.blindsjn.feature.foodcost.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.foodcost.model.*
import com.glowstudio.android.blindsjn.feature.foodcost.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    private val _recipeList = MutableStateFlow<List<Recipe>>(emptyList())
    val recipeList: StateFlow<List<Recipe>> = _recipeList

    fun registerRecipe(title: String, price: Long, businessId: Int, ingredients: List<RecipeIngredient>) {
        viewModelScope.launch {
            try {
                val request = RecipeRequest(title, price, businessId, ingredients)
                val response = RecipeRepository.registerRecipe(request)
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

    fun getRecipeList(businessId: Int) {
        viewModelScope.launch {
            try {
                val response = RecipeRepository.getRecipeList(businessId)
                if (response.isSuccessful) {
                    _recipeList.value = response.body()?.data ?: emptyList()
                }
            } catch (_: Exception) {}
        }
    }

    fun clearResult() {
        _registerResult.value = null
    }
} 