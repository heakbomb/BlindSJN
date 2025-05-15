/*
* 뉴스 세부 기사 확인 스크린
*
*
* */


package com.glowstudio.android.blindsjn.ui.screens

import android.text.Html
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.core.text.HtmlCompat
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.Alignment


@Composable
fun NewsDetailScreen(
    title: String,
    content: String?,
    description: String?,
    imageUrl: String?,
    link: String?
) {
    val context = LocalContext.current

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
            !link.isNullOrBlank() -> link
            else -> "내용이 없습니다."
        }

        Text(
            text = HtmlCompat.fromHtml(bodyText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 버튼 추가
        if (!link.isNullOrBlank()) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    context.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("전체 기사 보기")
            }
        }
    }
}
