package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id") val id: Int,
    @SerializedName("postId") val postId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("time") val time: String
)

data class CommentListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Comment>
)