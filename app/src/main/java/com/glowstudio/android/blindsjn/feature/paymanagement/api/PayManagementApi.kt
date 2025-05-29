package com.glowstudio.android.blindsjn.feature.paymanagement.api

import com.glowstudio.android.blindsjn.feature.paymanagement.model.SalesComparisonResponse
import com.glowstudio.android.blindsjn.feature.paymanagement.model.SalesSummaryResponse
import com.glowstudio.android.blindsjn.feature.paymanagement.model.TopItemsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PayManagementApi {
    @GET("api_sales_summary.php")
    suspend fun getSalesSummary(
        @Query("period") period: String,
        @Query("date") date: String
    ): SalesSummaryResponse

    @GET("api_sales_comparison.php")
    suspend fun getSalesComparison(
        @Query("date") date: String
    ): SalesComparisonResponse

    @GET("api_sales_top_items.php")
    suspend fun getTopItems(
        @Query("date") date: String,
        @Query("period") period: String = "day"
    ): TopItemsResponse
} 