package com.glowstudio.android.blindsjn.model

data class User(
    val userId: Int,
    val phoneNumber: String,
    val certifiedIndustries: List<IndustryType>
)