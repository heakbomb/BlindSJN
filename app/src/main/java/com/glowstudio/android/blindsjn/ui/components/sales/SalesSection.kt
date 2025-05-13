package com.glowstudio.android.blindsjn.ui.components.sales

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.ui.components.common.SectionLayout

/**
 * "오늘의 매출 관리" 섹션 UI를 표시하는 컴포저블 함수입니다.
 *
 * 섹션 레이아웃 내에 가로로 정렬된 Row를 포함하며, 향후 매출 관련 컴포넌트가 추가될 수 있습니다.
 */
@Composable
fun SalesSection() {
    SectionLayout(title = "오늘의 매출 관리") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
           // 여기에 뭐 넣을지 고민 좀 해봐야 함
        }
    }
} 