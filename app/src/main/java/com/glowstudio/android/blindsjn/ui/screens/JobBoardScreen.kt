package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.model.Post
import com.glowstudio.android.blindsjn.ui.viewModel.PostViewModel
import com.glowstudio.android.blindsjn.utils.TimeUtils.getTimeAgo
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobBoardScreen(
    navController: NavController,
    postViewModel: PostViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("전체", "카페", "음식점", "뷰티", "의료", "기타")
    var selectedTab by remember { mutableStateOf(0) }
    val posts by postViewModel.posts.collectAsState()

    LaunchedEffect(Unit) {
        postViewModel.loadPosts()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("writePost") }) {
                Icon(Icons.Filled.Edit, contentDescription = "글쓰기")
            }
        }
    ) { inner ->
        Column(Modifier.padding(inner)) {
            // 검색 바
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("제목 또는 내용을 검색") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // 탭
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                categories.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == idx)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // 필터 + 검색
            val filtered = remember(posts, selectedTab, searchQuery) {
                posts
                    .filter { selectedTab == 0 || it.industry?.displayName == categories[selectedTab] }
                    .filter { post ->
                        searchQuery.isBlank() ||
                                post.title.contains(searchQuery, true) ||
                                post.content.contains(searchQuery, true)
                    }
            }

            // 문자열 내림차순 정렬 (최신순)
            val sortedPosts = remember(filtered) {
                filtered.sortedByDescending { it.createdAt ?: "" }
            }

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sortedPosts) { post ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("postDetail/${post.id}") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            // 제목
                            Text(
                                post.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(4.dp))
                            // 내용
                            Text(
                                post.content,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Spacer(Modifier.height(8.dp))
                            // 메타 정보
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                // 시간
                                val ago = post.createdAt?.let { getTimeAgo(it) } ?: ""
                                if (ago.isNotEmpty()) {
                                    Text("⏱ $ago", style = MaterialTheme.typography.bodySmall)
                                }
                                // 댓글
                                Text("💬 ${post.commentCount}", style = MaterialTheme.typography.bodySmall)
                                // 좋아요
                                Text("👍 ${post.likeCount}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
