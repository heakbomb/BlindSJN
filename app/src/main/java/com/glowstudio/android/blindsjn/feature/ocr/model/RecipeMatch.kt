package com.glowstudio.android.blindsjn.feature.ocr.model

data class RecipeMatch(
    val recipeId: Int,
    val recipeName: String,
    val price: Int,
    val ingredientCost: Int,
    val margin: Int,
    val matchScore: Double
)

data class RecipeMatchRequest(
    val itemName: String,
    val price: Int
)

data class RecipeMatchResponse(
    val status: String,
    val message: String,
    val matches: List<RecipeMatch>
) 