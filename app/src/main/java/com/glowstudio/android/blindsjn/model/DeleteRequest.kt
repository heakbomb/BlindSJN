package com.glowstudio.android.blindsjn.model

import com.google.gson.annotations.SerializedName

data class DeleteRequest(
    @SerializedName("id") val id: Int
)
