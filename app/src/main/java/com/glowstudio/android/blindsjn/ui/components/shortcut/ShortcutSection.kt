package com.glowstudio.android.blindsjn.ui.components.shortcut

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.glowstudio.android.blindsjn.ui.components.common.SectionLayout

data class ShortcutItem(
    val title: String,
    val emoji: String
)

private val shortcutItems = listOf(
    ShortcutItem("푸드코스트", "🍴"),
    ShortcutItem("캘린더", "📅")
)

/**
 * 바로가기 섹션을 가로 스크롤 가능한 형태로 표시하는 컴포저블입니다.
 *
 * 미리 정의된 바로가기 항목들을 아이콘과 함께 보여주며, 각 항목을 클릭하면 해당 목적지로 네비게이션합니다.
 * "푸드코스트" 클릭 시 "foodCost" 경로로, "캘린더" 클릭 시 "message" 경로로 이동합니다.
 */
@Composable
fun ShortcutSection(navController: NavHostController) {
    SectionLayout(title = "바로가기") {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(shortcutItems) { item ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .clickable {
                            when (item.title) {
                                "푸드코스트" -> navController.navigate("foodCost")
                                "캘린더" -> navController.navigate("message")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.emoji,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
} 