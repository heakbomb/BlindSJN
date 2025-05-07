/*
* 뉴스 기사 아티클
*
* */

package com.glowstudio.android.blindsjn.model

import java.io.Serializable

data class Article(
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val link: String?
) : Serializable

data class Source(
    val id: String?,
    val name: String?
) : Serializable