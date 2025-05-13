package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi

/**
 * 사용자가 태그 목록에서 여러 개의 태그를 선택할 수 있는 바텀시트 UI를 표시합니다.
 *
 * 선택 가능한 태그와 전체 태그 목록을 받아, 사용자가 태그를 선택 및 해제할 수 있도록 하며,
 * 최소 한 개 이상의 태그가 선택된 경우에만 "작성하기" 버튼이 활성화됩니다.
 * 버튼 클릭 시 현재 선택된 태그 목록이 콜백으로 전달됩니다.
 *
 * @param tags 전체 표시할 태그 문자열 목록입니다.
 * @param enabledTags 선택 가능하도록 활성화할 태그 목록입니다. 기본값은 전체 태그입니다.
 * @param onDone 사용자가 태그 선택을 완료하고 제출할 때 선택된 태그 목록을 전달받는 콜백입니다.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostBottomSheet(
    tags: List<String>,
    enabledTags: List<String> = tags,
    onDone: (List<String>) -> Unit
) {
    val selectedTags = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text("태그를 선택하세요", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                val enabled = enabledTags.contains(tag)
                val selected = selectedTags.contains(tag)
                TagChip(
                    text = tag,
                    selected = selected,
                    enabled = enabled,
                    onClick = {
                        if (enabled) {
                            if (selected) selectedTags.remove(tag)  
                            else selectedTags.add(tag)
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        CommonButton(
            text = "작성하기",
            onClick = { onDone(selectedTags.toList()) },
            enabled = selectedTags.isNotEmpty()
        )
    }
}

/**
 * 선택 가능 여부와 선택 상태에 따라 스타일이 달라지는 태그 칩을 표시합니다.
 *
 * @param text 태그에 표시할 텍스트
 * @param selected 태그가 선택된 상태인지 여부
 * @param enabled 태그가 선택 가능 상태인지 여부
 * @param onClick 태그 클릭 시 호출되는 콜백
 */
@Composable
fun TagChip(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val background = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        selected -> MaterialTheme.colorScheme.primary
        else -> Color.White
    }
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        selected -> Color.White
        else -> MaterialTheme.colorScheme.primary
    }
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = background,
        border = if (!selected && enabled) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = Modifier
            .alpha(if (enabled) 1f else 0.5f)
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

/**
 * `PostBottomSheet`의 미리보기를 샘플 태그와 함께 표시합니다.
 *
 * Compose 프리뷰에서 다양한 활성/비활성 태그 상태와 UI 구성을 시각적으로 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun PostBottomSheetPreview() {
    val tags = listOf("누구나", "재학생", "패션디자인", "비활성1", "비활성2")
    val enabledTags = listOf("누구나", "재학생", "패션디자인")
    PostBottomSheet(
        tags = tags,
        enabledTags = enabledTags,
        onDone = {}
    )
} 