package com.glowstudio.android.blindsjn.feature.ocr.api

import com.glowstudio.android.blindsjn.feature.ocr.model.OcrRequest
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NaverOcrApiService {
    @POST("document/receipt")
    suspend fun performOcr(
        @Body request: OcrRequest
    ): OcrResponse
} 