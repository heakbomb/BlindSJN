package com.glowstudio.android.blindsjn.feature.ocr.model

import com.google.gson.annotations.SerializedName

data class OcrRequest(
    val version: String = "V2",
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImage>
)

data class OcrImage(
    val format: String = "jpg",
    val data: String,  // Base64 encoded image
    val name: String
)

data class OcrResponse(
    val version: String,
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImageResult>
)

data class OcrImageResult(
    val uid: String,
    val name: String,
    val inferResult: String,
    val message: String,
    val validationResult: ValidationResult,
    val fields: List<Field>
)

data class ValidationResult(
    val result: String
)

data class Field(
    val valueType: String,
    val boundingPolys: List<BoundingPoly>,
    val inferText: String,
    val inferConfidence: Double,
    val type: String,
    val value: String
)

data class BoundingPoly(
    val vertices: List<Vertex>
)

data class Vertex(
    val x: Int,
    val y: Int
)

data class OcrResult(
    val text: String,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis()
) 