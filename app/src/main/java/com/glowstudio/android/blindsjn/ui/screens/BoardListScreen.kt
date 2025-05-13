// BoardListScreen.kt
package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.model.BoardCategory
import com.glowstudio.android.blindsjn.model.IndustryType
import com.glowstudio.android.blindsjn.model.Post
import com.glowstudio.android.blindsjn.model.User
import com.glowstudio.android.blindsjn.utils.TimeUtils.getTimeAgo

@Composable
fun BoardListScreen(
    navController: NavController,
    category: BoardCategory,
    posts: List<Post>,
    user: User
) {
    // 업종필터 선택 상태
    var selectedIndustry by remember { mutableStateOf<IndustryType?>(null) }

    Column(Modifier.fillMaxSize()) {
        // 업종게시판일 때만 칩 필터 표시
        if (category == BoardCategory.INDUSTRY) {
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedIndustry == null,
                        onClick  = { selectedIndustry = null },
                        label    = { Text("전체") }
                    )
                }
                items(user.certifiedIndustries) { industry ->
                    FilterChip(
                        selected = selectedIndustry == industry,
                        onClick  = {
                            selectedIndustry =
                                if (selectedIndustry == industry) null else industry
                        },
                        label = { Text(industry.displayName) }
                    )
                }
            }
        }

        // 1) 카테고리·업종 필터링
        val filtered = remember(posts, category, selectedIndustry) {
            posts.filter { post ->
                when (category) {
                    BoardCategory.INDUSTRY ->
                        selectedIndustry == null || post.industry == selectedIndustry
                    BoardCategory.FREE     ->
                        post.category == BoardCategory.FREE
                    BoardCategory.QUESTION ->
                        post.category == BoardCategory.QUESTION
                    BoardCategory.POPULAR  ->
                        true
                }
            }
        }

        // 2) 정렬: 인기게시판은 좋아요, 나머지는 최신순
        val sorted = remember(filtered, category) {
            if (category == BoardCategory.POPULAR) {
                filtered.sortedByDescending { it.likeCount }
            } else {
                filtered.sortedByDescending { post ->
                    // createdAt 이 null 이면 0L
                    post.createdAt
                        ?.let { runCatching { getTimeAgo(it); System.currentTimeMillis() }.getOrNull() }
                        ?: 0L
                }
            }
        }

        // 3) 리스트 표시
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sorted) { post ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("postDetail/${post.id}") },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        // 제목 (한 줄, Bold)
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(4.dp))

                        // 내용 (두 줄, 연하게)
                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(8.dp))

                        // 메타 정보 (시간 · 댓글 · 좋아요)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // createdAt 이 null 이면 빈 문자열
                            val timeText = post.createdAt?.let { getTimeAgo(it) } ?: ""
                            Text("⏱ $timeText", style = MaterialTheme.typography.bodySmall)
                            Text("💬 ${post.commentCount}", style = MaterialTheme.typography.bodySmall)
                            Text("👍 ${post.likeCount}",   style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
