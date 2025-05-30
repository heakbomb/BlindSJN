package com.glowstudio.android.blindsjn.network

import com.glowstudio.android.blindsjn.feature.ocr.api.NaverOcrApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private const val BASE_URL = "https://api.example.com/"  // Replace with actual base URL
    private const val NAVER_OCR_BASE_URL = "https://d7cblqkw1o.apigw.ntruss.com/custom/v1/42447/02392618f64f8a5d3fd1004abc08eb2e9cd4a9eba2e80c21b5ccdf7fa7b73df4/document/receipt"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val naverOcrRetrofit = Retrofit.Builder()
        .baseUrl(NAVER_OCR_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // ... existing API services ...

    val naverOcrApiService: NaverOcrApiService = naverOcrRetrofit.create(NaverOcrApiService::class.java)
} 