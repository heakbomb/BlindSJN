package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int? = null // 👈 여기를 추가!
)
