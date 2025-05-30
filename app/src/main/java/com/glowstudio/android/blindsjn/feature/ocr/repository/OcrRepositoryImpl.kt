package com.glowstudio.android.blindsjn.feature.ocr.repository

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.glowstudio.android.blindsjn.data.network.NaverOcrServer
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrImage
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrRequest
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
import java.util.UUID

class OcrRepositoryImpl : OcrRepository {
    private val apiService = NaverOcrServer.apiService
    private val TAG = "OcrRepositoryImpl"

    override suspend fun processImage(bitmap: Bitmap): Flow<OcrResult> = flow {
        try {
            // Convert bitmap to base64
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

            // Create OCR request
            val request = OcrRequest(
                requestId = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis(),
                images = listOf(
                    OcrImage(
                        data = base64Image,
                        name = "image.jpg"
                    )
                )
            )

            // Call Naver OCR API
            val response = apiService.performOcr(request)
            Log.d(TAG, "OCR Response: $response")

            // Process response
            if (response.images.isNotEmpty()) {
                val imageResult = response.images[0]
                Log.d(TAG, "Image Result: $imageResult")
                
                if (imageResult.inferResult == "SUCCESS") {
                    // Process receipt fields
                    val items = mutableListOf<String>()
                    var totalAmount = ""
                    var date = ""

                    imageResult.fields.forEach { field ->
                        when (field.type) {
                            "item" -> items.add(field.inferText)
                            "total_amount" -> totalAmount = field.inferText
                            "date" -> date = field.inferText
                        }
                        Log.d(TAG, "Field: type=${field.type}, text=${field.inferText}, confidence=${field.inferConfidence}")
                    }

                    // Create structured result
                    val resultText = buildString {
                        if (date.isNotEmpty()) append("날짜: $date\n")
                        if (items.isNotEmpty()) {
                            append("\n상품 목록:\n")
                            items.forEach { append("• $it\n") }
                        }
                        if (totalAmount.isNotEmpty()) append("\n총액: $totalAmount")
                    }

                    if (resultText.isBlank()) {
                        throw Exception("상품정보를 찾을 수 없습니다")
                    }

                    val confidence = imageResult.fields.map { it.inferConfidence }.average().toFloat()
                    emit(OcrResult(resultText, confidence))
                } else {
                    Log.e(TAG, "OCR failed: ${imageResult.message}")
                    throw Exception("OCR failed: ${imageResult.message}")
                }
            } else {
                Log.e(TAG, "No OCR results found")
                throw Exception("No OCR results found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to process image", e)
            throw Exception("Failed to process image: ${e.message}")
        }
    }

    override suspend fun saveOcrResult(result: OcrResult) {
        TODO("Not yet implemented")
    }

    override fun getOcrHistory(): Flow<List<OcrResult>> {
        TODO("Not yet implemented")
    }
} 