package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("category")
    val category: BoardCategory,

    @SerializedName("industry")
    val industry: IndustryType?,

    @SerializedName("user_id")
    val userId: Int,

    // Date? → String 으로 변경했습니다.
    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("comment_count")
    val commentCount: Int = 0,

    @SerializedName("like_count")
    val likeCount: Int = 0,

    @SerializedName("is_liked")
    val isLiked: Boolean = false
)


// 게시글 응답 모델
data class PostListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Post>
)
