// CategoryListScreen.kt
package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    onCategoryClick: (String) -> Unit
) {
    val tabs = listOf("게시판", "진로", "홍보", "단체")
    var selectedTab by remember { mutableStateOf(0) }

    // 각 탭별 실제 게시판 리스트
    val boards = remember {
        mapOf(
            0 to listOf("졸업생게시판", "시사·이슈", "수원캠 장터게시판", "정보게시판",
                "수원캠 자유게시판", "서울캠 자유게시판", "새내기게시판", "서울캠 장터게시판"),
            1 to listOf("오늘의 학식", "강의평가", "스터디", "책방"),
            2 to listOf("홍보게시판1", "홍보게시판2", "홍보게시판3"),
            3 to listOf("단체게시판1", "단체게시판2", "단체게시판3")
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 탭
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // 탭 아래 리스트
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(boards[selectedTab].orEmpty()) { board ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(board) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (selectedTab) {
                            0 -> Icons.Default.BookmarkBorder
                            1 -> Icons.Default.School
                            2 -> Icons.Default.Campaign
                            3 -> Icons.Default.Group
                            else -> Icons.Default.List
                        },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = board, style = MaterialTheme.typography.bodyLarge)
                }
                Divider()
            }
        }
    }
}
