package com.glowstudio.android.blindsjn.feature.ocr.network

import com.glowstudio.android.blindsjn.feature.ocr.model.*
import retrofit2.Response
import retrofit2.http.*

interface OcrApiService {
    // 영수증 이미지 분석 API
    @POST("document/receipt")
    suspend fun analyzeReceipt(
        @Body request: OcrRequest
    ): Response<OcrApiResponse>

    // OCR 분석 결과 저장 API
    @POST("api_save_ocr_result.php")
    suspend fun saveOcrResult(@Body request: OcrSaveRequest): OcrSaveResponse
}

data class OcrRequest(
    val version: String,
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImage>
)

data class OcrImage(
    val format: String,
    val name: String
)

data class OcrSaveRequest(
    val date: String,
    val items: List<OcrItem>
)

data class OcrItem(
    val name: String,
    val quantity: Int,
    val price: Int
)

data class OcrSaveResponse(
    val status: String,
    val message: String,
    val data: OcrSaveData?
)

data class OcrSaveData(
    val date: String,
    val total_sales_amount: Double,
    val total_margin_amount: Double,
    val day_of_week: String
) 