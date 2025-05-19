package com.glowstudio.android.blindsjn.feature.board.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category: String,
    @SerializedName("experience") val experience: String = "",
    @SerializedName("time") val time: String,  // ← 이게 있어야 합니다
    @SerializedName("commentCount") val commentCount: String,
    @SerializedName("likeCount") val likeCount: String,
    @SerializedName("isLiked") val isLiked: Boolean = false
) {
    // 문자열로 된 카운트를 Int로 변환하는 확장 프로퍼티
    val likeCountInt: Int
        get() = likeCount.toIntOrNull() ?: 0

    val commentCountInt: Int
        get() = commentCount.toIntOrNull() ?: 0
}