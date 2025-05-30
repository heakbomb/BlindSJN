package com.glowstudio.android.blindsjn.feature.ocr.model

import com.google.gson.annotations.SerializedName

/**
 * Request model for Naver OCR API
 */
data class OcrRequest(
    val version: String = "V2",
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImage>
)

/**
 * Image data for OCR request
 */
data class OcrImage(
    val format: String = "jpg",
    val data: String,  // Base64 encoded image
    val name: String
)

/**
 * Response model from Naver OCR API
 */
data class OcrResponse(
    val version: String,
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImageResult>
)

/**
 * Result for a single image in the OCR response
 */
data class OcrImageResult(
    val uid: String,
    val name: String,
    val inferResult: String,  // "SUCCESS" or "FAIL"
    val message: String,
    val validationResult: ValidationResult,
    val fields: List<Field>
)

/**
 * Validation result for the OCR response
 */
data class ValidationResult(
    val result: String
)

/**
 * Recognized field from the receipt
 * Field types include:
 * - "item": Product/item name
 * - "total_amount": Total payment amount
 * - "date": Receipt date
 */
data class Field(
    val valueType: String,
    val boundingPolys: List<BoundingPoly>,
    val inferText: String,
    val inferConfidence: Double,
    val type: String,
    val value: String
)

/**
 * Bounding polygon for a recognized field
 */
data class BoundingPoly(
    val vertices: List<Vertex>
)

/**
 * Vertex coordinates for bounding polygon
 */
data class Vertex(
    val x: Int,
    val y: Int
)

/**
 * Processed OCR result for UI display
 */
data class OcrResult(
    val text: String,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis()
) 