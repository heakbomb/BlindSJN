package com.glowstudio.android.blindsjn.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 제목과 선택적 더보기 버튼, 그리고 사용자 정의 콘텐츠를 세로로 배치하는 컴포저블 레이아웃입니다.
 *
 * @param title 상단에 굵게 표시될 제목 텍스트
 * @param onMoreClick 더보기 아이콘 버튼 클릭 시 호출되는 콜백 (선택적)
 * @param content 제목 아래에 표시될 사용자 정의 컴포저블 콘텐츠
 */
@Composable
fun SectionLayout(
    title: String,
    onMoreClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            IconButton(onClick = { onMoreClick?.invoke() }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "더보기")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
} 