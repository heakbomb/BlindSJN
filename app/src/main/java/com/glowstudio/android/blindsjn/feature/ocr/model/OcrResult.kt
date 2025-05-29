package com.glowstudio.android.blindsjn.feature.ocr.model

data class OcrResult(
    val name: String,
    val quantity: Int,
    val price: Int,
    val recipeId: Int? = null
)

data class OcrResultList(
    val items: List<OcrResult>
) 