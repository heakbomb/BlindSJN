/*
* 뉴스 세부 기사 확인 스크린
*
*
* */


package com.glowstudio.android.blindsjn.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.core.text.HtmlCompat

@Composable
fun NewsDetailScreen(title: String, content: String?, description: String?, imageUrl: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "기사 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        val bodyText = when {
            !content.isNullOrBlank() -> content
            !description.isNullOrBlank() -> description
            else -> "내용이 없습니다."
        }

        Text(
            text = HtmlCompat.fromHtml(bodyText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}