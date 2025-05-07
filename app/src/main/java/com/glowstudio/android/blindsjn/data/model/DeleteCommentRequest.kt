package com.glowstudio.android.blindsjn.data.model

import com.google.gson.annotations.SerializedName

data class DeleteCommentRequest(
    @SerializedName("comment_id") val commentId: Int
)
