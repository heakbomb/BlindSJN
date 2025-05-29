package com.glowstudio.android.blindsjn.feature.ocr.model

data class OcrApiResponse(
    val version: String,
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImage>
)

data class OcrImage(
    val uid: String,
    val name: String,
    val inferResult: String,
    val message: String,
    val fields: List<OcrField>
)

data class OcrField(
    val valueType: String,
    val boundingPoly: BoundingPoly,
    val inferText: String,
    val inferConfidence: Double
)

data class BoundingPoly(
    val vertices: List<Vertex>
)

data class Vertex(
    val x: Int,
    val y: Int
) 