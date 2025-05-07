
package com.glowstudio.android.blindsjn.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BusinessApiService {
    // 사업자등록정보 진위확인 및 상태조회
    @GET("nts-businessman/v1/status")
    suspend fun checkBusinessNumberValidity(
        @Query("serviceKey") serviceKey: String,
        @Query("b_no") businessNumber: String
    ): Response<BusinessCheckResponse>
}
