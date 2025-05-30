package com.glowstudio.android.blindsjn.feature.ocr.repository

import android.graphics.Bitmap
import android.util.Base64
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

            // Process response
            if (response.images.isNotEmpty()) {
                val imageResult = response.images[0]
                if (imageResult.inferResult == "SUCCESS") {
                    // Combine all detected text
                    val text = imageResult.fields.joinToString("\n") { it.inferText }
                    val confidence = imageResult.fields.map { it.inferConfidence }.average().toFloat()
                    
                    emit(OcrResult(text, confidence))
                } else {
                    throw Exception("OCR failed: ${imageResult.message}")
                }
            } else {
                throw Exception("No OCR results found")
            }
        } catch (e: Exception) {
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