package com.glowstudio.android.blindsjn.feature.ocr.model

data class DailySalesRequest(
    val date: String,
    val total_sales_amount: Double,
    val total_margin_amount: Double,
    val day_of_week: String,
    val items: List<DailySalesItem>
)

data class DailySalesItem(
    val recipe_id: Int?,
    val recipe_name: String,
    val quantity: Int,
    val price: Int,
    val total_amount: Int,
    val ingredient_cost: Int?,
    val margin: Int?
)

data class DailySalesResponse(
    val status: String,
    val message: String,
    val data: DailySalesData?
)

data class DailySalesData(
    val date: String,
    val total_sales_amount: Double,
    val total_margin_amount: Double,
    val day_of_week: String
)

data class RecipeMarginSummaryRequest(
    val date: String,
    val recipe_id: Int,
    val recipe_name: String,
    val total_sales: Int,
    val total_margin: Int
)

data class RecipeMarginSummaryResponse(
    val status: String,
    val message: String
)

data class DailySummaryRequest(
    val date: String,
    val total_sales: Double,
    val total_margin: Double,
    val margin_rate: Double
)

data class DailySummaryResponse(
    val status: String,
    val message: String
) 