package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.feature.board.model.Post
import com.glowstudio.android.blindsjn.feature.board.viewmodel.*
import androidx.compose.ui.graphics.Color
import com.glowstudio.android.blindsjn.ui.theme.*
import com.glowstudio.android.blindsjn.feature.board.view.PostBottomSheet
import com.glowstudio.android.blindsjn.feature.board.viewmodel.PostBottomSheetViewModel
import java.net.URLEncoder
import com.glowstudio.android.blindsjn.utils.TimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(navController: NavController, title: String) {
    val viewModel: PostViewModel = viewModel()
    val posts by viewModel.posts.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    var selectedCategory by remember { mutableStateOf("모든 분야") }
    val categories = listOf("모든 분야", "카페", "식당", "배달 전문", "패스트푸드", "호텔")

    val postBottomSheetViewModel: PostBottomSheetViewModel = viewModel()
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            val tags by postBottomSheetViewModel.tags.collectAsState()
            val enabledTags by postBottomSheetViewModel.enabledTags.collectAsState()
            val selectedTags by postBottomSheetViewModel.selectedTags.collectAsState()
            PostBottomSheet(
                tags = tags,
                enabledTags = enabledTags,
                onDone = {
                    showSheet = false
                    val encodedTags = URLEncoder.encode(it.joinToString(","), "UTF-8")
                    navController.navigate("writePost?tags=$encodedTags")
                    postBottomSheetViewModel.clearSelection()
                }
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "글쓰기")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (title == "industry") {
                    CategoryFilterRow(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

                val filteredPosts = when (title) {
                    "free" -> posts.filter { it.category == "자유게시판" }
                    "question" -> posts.filter { it.category == "질문게시판" }
                    "hot" -> posts.filter { it.category == "인기게시판" }
                    "industry" -> posts.filter { selectedCategory == "모든 분야" || it.category.contains(selectedCategory) }
                    else -> posts
                }

                if (!statusMessage.isNullOrEmpty()) {
                    Text(
                        text = statusMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                PostList(navController, filteredPosts, viewModel)
            }
        }
    )
}

@Composable
fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                text = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PostList(
    navController: NavController,
    posts: List<Post>,
    viewModel: PostViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(posts) { post ->
            PostItem(navController, post, viewModel)
        }
    }
}

@Composable
fun PostItem(
    navController: NavController,
    post: Post,
    viewModel: PostViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .clickable { navController.navigate("postDetail/${post.id}") }
            .padding(16.dp)
    ) {
        Text(
            text = post.category + " | " + post.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\u23F1 " + TimeUtils.getTimeAgo(post.time),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Filled.ChatBubbleOutline,
                contentDescription = "댓글",
                tint = ChatBlue,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${post.commentCount}",
                style = MaterialTheme.typography.bodySmall,
                color = ChatBlue
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Filled.ThumbUp,
                contentDescription = "좋아요",
                tint = if (post.isLiked) Error else DividerGray,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${post.likeCount}",
                style = MaterialTheme.typography.bodySmall,
                color = if (post.isLiked) Error else DividerGray
            )
        }
    }
}
