package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class PostRequest(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("industry") val industry: String
)
