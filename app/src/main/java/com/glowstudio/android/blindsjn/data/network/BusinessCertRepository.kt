package com.glowstudio.android.blindsjn.data.network

import android.util.Log
import com.glowstudio.android.blindsjn.data.model.BusinessCertRequest
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

    // 사업자 인증 정보 저장
    suspend fun saveBusinessCertification(
        userId: Int,
        phoneNumber: String,
        businessNumber: String,
        industryId: Int
    ): Boolean {
        return try {
            val request = BusinessCertRequest(
                user_id = userId,
                phone_number = phoneNumber,
                business_number = businessNumber,
                industry_id = industryId
            )

            val response = InternalServer.api.certifyBusiness(request)
            
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return it.status == "success"
                }
            }
            false
        } catch (e: Exception) {
            Log.e("BusinessCertRepository", "인증 정보 저장 실패: ${e.message}")
            false
        }
    }
}