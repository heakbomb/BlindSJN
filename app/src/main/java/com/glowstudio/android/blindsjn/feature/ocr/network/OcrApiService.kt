package com.glowstudio.android.blindsjn.feature.ocr.network

import com.glowstudio.android.blindsjn.feature.ocr.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface OcrApiService {
    // 영수증 이미지 분석 API
    @Multipart
    @POST("document/receipt")
    suspend fun analyzeReceipt(
        @Header("X-OCR-SECRET") secretKey: String,
        @Part image: MultipartBody.Part
    ): Response<OcrApiResponse>

    // OCR 분석 결과 저장 API
    @POST("api_save_ocr_result.php")
    suspend fun saveOcrResult(@Body request: OcrSaveRequest): OcrSaveResponse
}

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