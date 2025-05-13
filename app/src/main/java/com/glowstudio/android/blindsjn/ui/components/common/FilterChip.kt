package com.glowstudio.android.blindsjn.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.ui.theme.*

/**
 * 선택 여부에 따라 스타일이 변경되는 필터 칩 UI 컴포저블을 표시합니다.
 *
 * @param text 칩에 표시할 텍스트 레이블입니다.
 * @param isSelected 칩의 선택 상태를 나타냅니다. 선택 시 배경과 텍스트 색상이 변경됩니다.
 * @param onClick 칩이 클릭될 때 호출되는 콜백입니다.
 */
@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .background(
                color = if (isSelected) Blue else DividerGray,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() },
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) White else TextPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

/**
 * 선택된 상태의 FilterChip 컴포넌트 미리보기를 표시합니다.
 *
 * BlindSJNTheme 내에서 "선택됨" 텍스트와 선택된 상태로 FilterChip을 렌더링합니다.
 */
@Preview(showBackground = true)
@Composable
fun FilterChipSelectedPreview() {
    BlindSJNTheme {
        FilterChip(
            text = "선택됨",
            isSelected = true,
            onClick = {}
        )
    }
}

/**
 * 선택되지 않은 상태의 FilterChip 컴포넌트 미리보기를 제공합니다.
 *
 * 앱 테마 내에서 "선택안됨" 텍스트와 함께 비선택 상태의 FilterChip을 시각적으로 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun FilterChipUnselectedPreview() {
    BlindSJNTheme {
        FilterChip(
            text = "선택안됨",
            isSelected = false,
            onClick = {}
        )
    }
}

