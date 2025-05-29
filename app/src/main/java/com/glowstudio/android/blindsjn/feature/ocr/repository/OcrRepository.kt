package com.glowstudio.android.blindsjn.feature.ocr.repository

import android.content.Context
import android.net.Uri
import com.glowstudio.android.blindsjn.data.network.isNetworkAvailable
import com.glowstudio.android.blindsjn.data.network.OcrApiServer
import com.glowstudio.android.blindsjn.feature.ocr.model.*
import com.glowstudio.android.blindsjn.feature.ocr.network.OcrItem
import com.glowstudio.android.blindsjn.feature.ocr.network.OcrSaveRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class OcrRepository {
    companion object {
        private const val API_KEY = "bmxVU1pnQUhxVFBKWnJlb3RkaE9ybnRId0NkQ3lOQ3Q="
    }

    // 영수증 이미지를 분석하여 상품 정보 추출
    suspend fun analyzeReceipt(uri: Uri): Result<List<OcrResult>> {
        return try {
            val file = File(uri.path!!)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = OcrApiServer.apiService.analyzeReceipt(API_KEY, imagePart)
            
            if (response.isSuccessful) {
                val ocrResponse = response.body()
                if (ocrResponse != null) {
                    val results = parseOcrResponse(ocrResponse)
                    if (results.isNotEmpty()) {
                        Result.success(results)
                    } else {
                        Result.failure(Exception("영수증에서 상품 정보를 찾을 수 없습니다."))
                    }
                } else {
                    Result.failure(Exception("응답 데이터가 없습니다."))
                }
            } else {
                Result.failure(Exception("OCR 분석에 실패했습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // OCR 분석 결과를 서버에 저장
    suspend fun saveOcrResults(results: List<OcrResult>): Result<Unit> {
        return try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val request = OcrSaveRequest(
                date = currentDate,
                items = results.map { result ->
                    OcrItem(
                        name = result.name,
                        quantity = result.quantity,
                        price = result.price
                    )
                }
            )

            val response = OcrApiServer.apiService.saveOcrResult(request)
            if (response.status == "success") {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // OCR API 응답을 파싱하여 상품 정보 추출
    private fun parseOcrResponse(response: OcrApiResponse): List<OcrResult> {
        val results = mutableListOf<OcrResult>()
        
        response.images.firstOrNull()?.fields?.let { fields ->
            var currentName = ""
            var currentQuantity = 0
            var currentPrice = 0
            
            fields.forEach { field ->
                val text = field.inferText.trim()
                
                // 상품명 추출
                if (text.matches(Regex("^[가-힣a-zA-Z\\s]+$"))) {
                    if (currentName.isNotEmpty() && currentPrice > 0) {
                        results.add(OcrResult(currentName, currentQuantity, currentPrice))
                    }
                    currentName = text
                    currentQuantity = 1
                }
                
                // 수량 추출
                if (text.matches(Regex("^\\d+$"))) {
                    val number = text.toIntOrNull()
                    if (number != null && number < 100) {
                        currentQuantity = number
                    }
                }
                
                // 가격 추출
                if (text.matches(Regex(".*\\d+$"))) {
                    val price = text.replace(Regex("[^0-9]"), "").toIntOrNull()
                    if (price != null && price > 0) {
                        currentPrice = price
                    }
                }
            }
            
            // 마지막 상품 정보 저장
            if (currentName.isNotEmpty() && currentPrice > 0) {
                results.add(OcrResult(currentName, currentQuantity, currentPrice))
            }
        }
        
        return results
    }
} 