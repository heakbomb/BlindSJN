package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class PostDetailResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Post? // 단일 게시글 객체
)
