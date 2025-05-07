package com.glowstudio.android.blindsjn.data.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment_id") val commentId: Int,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String
)



data class CommentListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Comment>
)
