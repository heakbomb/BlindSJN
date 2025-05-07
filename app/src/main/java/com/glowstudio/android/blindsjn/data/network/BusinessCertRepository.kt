package com.glowstudio.android.blindsjn.data.network

import android.util.Log
import retrofit2.Response

object BusinessCertRepository {

    private const val SERVICE_KEY = "oMaLHlh2WsmH9%2FejOxmPKKA8tV7MiQNcyt2HF9ca0cCQfUdUoNisHpBiYMJfzh%2BGzQYLNrJSaw5yKyAJWUz7cg%3D%3D" // 인증키 (Encoding)

    // 사업자 등록번호 진위 확인 API 호출
    suspend fun checkBusinessNumberValidity(businessNumber: String): Boolean {
        return try {
            val response: Response<BusinessCheckResponse> = PublicApiRetrofitInstance.api.checkBusinessNumberValidity(
                SERVICE_KEY, businessNumber)

            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    // status 값이 "01"이면 유효한 사업자번호
                    if (it.status == "01") {
                        return true // 인증 성공
                    }
                }
            }
            false // 인증 실패
        } catch (e: Exception) {
            Log.e("BusinessCertRepository", "API 호출 실패: ${e.message}")
            false
        }
    }

    // 이미 인증된 번호인지 확인하는 함수 (DB나 SharedPreferences에서 확인)
    suspend fun checkAlreadyCertified(businessNumber: String): Boolean {
        // 예시로 DB나 SharedPreferences에서 확인 (실제 구현은 DB 연동 필요)
        return false // 실제 DB 연동 시 true로 반환하도록 변경
    }
}