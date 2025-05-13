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

/**
 * 게시판 상세 화면을 표시하며, 카테고리별 게시글 목록과 글쓰기 기능을 제공합니다.
 *
 * 선택된 카테고리에 따라 게시글을 필터링하여 보여주고, 플로팅 액션 버튼을 통해 태그 선택 바텀시트를 띄워 글쓰기 화면으로 이동할 수 있습니다.
 * 상태 메시지가 있을 경우 화면 상단에 표시됩니다.
 *
 * @param navController 네비게이션을 위한 컨트롤러
 * @param title 화면 상단에 표시될 제목
 */
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
                CategoryFilterRow(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )

                val filteredPosts = posts.filter { post ->
                    selectedCategory == "모든 분야" || post.category.contains(selectedCategory)
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

/**
 * 게시글의 제목, 내용, 카테고리, 경험치, 좋아요 및 댓글 수를 표시하는 카드형 아이템을 렌더링합니다.
 *
 * 게시글을 클릭하면 해당 게시글의 상세 화면으로 이동합니다.  
 * 좋아요 아이콘을 클릭하면 좋아요 수가 증가하며, 한 번만 누를 수 있습니다.
 *
 * @param post 표시할 게시글 데이터
 */
@Composable
fun PostItem(
    navController: NavController,
    post: Post,
    viewModel: PostViewModel
) {
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(post.likeCount) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .clickable { navController.navigate("postDetail/${post.id}") }
            .padding(16.dp)
    ) {
        Text(text = post.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${post.category} ${post.experience}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(   
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = "좋아요",
                    tint = if (isLiked) Error else DividerGray,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            if (!isLiked) {
                                likeCount++
                                isLiked = true
                                viewModel.incrementLike(post.id)
                            }
                        }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$likeCount",
                    color = if (isLiked) Error else DividerGray,
                    style = MaterialTheme.typography.bodySmall
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
                    color = ChatBlue,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
