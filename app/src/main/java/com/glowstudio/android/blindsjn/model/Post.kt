package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("industry")
    val industry: String,
    @SerializedName("like_count")
    val likeCount: Int,
    @SerializedName("comment_count")
    val commentCount: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("is_liked")
    val isLiked: Boolean = false  // 기본값을 false로 설정
)
//게시글 응답모델
data class PostListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Post>
)
