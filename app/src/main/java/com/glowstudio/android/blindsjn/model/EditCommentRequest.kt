package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class EditCommentRequest(
    @SerializedName("comment_id") val commentId: Int,
    @SerializedName("content") val content: String
)
