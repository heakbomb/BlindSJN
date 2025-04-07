package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment_id") val commentId: Int,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("comment_text") val content: String?, // ← nullable 처리
    @SerializedName("created_at") val createdAt: String
)

data class CommentListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Comment>
)
