package com.glowstudio.android.blindsjn.feature.foodcost.model

data class Recipe(
    val recipe_id: Int? = null,
    val title: String,
    val price: Long,
    val margin_info: MarginInfo? = null
)

data class MarginInfo(
    val total_ingredient_price: Int = 0,
    val margin: Int = 0,
    val margin_percentage: Double = 0.0
)

data class RecipeIngredient(
    val name: String,
    val grams: Double
)

data class RecipeRequest(
    val title: String,
    val price: Long,
    val business_id: Int,
    val ingredients: List<RecipeIngredient>
) 