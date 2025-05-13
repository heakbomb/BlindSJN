package com.glowstudio.android.blindsjn.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glowstudio.android.blindsjn.model.Post
import com.glowstudio.android.blindsjn.ui.viewModel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

/**
 * 게시판 상세 화면을 표시합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(
    category: String,
    onBackClick: () -> Unit,
    onPostClick: (Int) -> Unit,
    postViewModel: PostViewModel
) {
    var selectedSort by remember { mutableStateOf("최신순") }
    var selectedCategory by remember { mutableStateOf(category) }
    val posts by postViewModel.posts.collectAsState()

    // 화면에 진입할 때 한 번만 호출하여 게시글을 불러옵니다.
    LaunchedEffect(Unit) {
        postViewModel.loadPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 앱바
        CenterAlignedTopAppBar(
            title = { Text("게시판") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                }
            }
        )

        // 카테고리 필터
        CategoryFilterRow(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )

        // 정렬 표시 및 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedSort,
                fontSize = 14.sp,
                color = Color.Gray
            )
            IconButton(onClick = { /* TODO: 정렬 옵션 표시 */ }) {
                Icon(Icons.Filled.Sort, contentDescription = "정렬")
            }
        }

        // 게시글 리스트
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { post ->
                PostItem(
                    post = post,
                    onClick = { onPostClick(post.id) },
                    postViewModel = postViewModel
                )
            }
        }
    }
}

/**
 * 카테고리 필터 Row
 */
@Composable
fun CategoryFilterRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("전체", "카페", "건강", "일상", "기타")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            CategoryChip(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

/**
 * 단일 카테고리 칩
 */
@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF5F5F5)
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (isSelected) Color(0xFF2E7D32) else Color.Gray,
            fontSize = 14.sp
        )
    }
}

/**
 * 게시글 아이템
 */
@Composable
fun PostItem(
    post: Post,
    onClick: () -> Unit,
    postViewModel: PostViewModel
) {
    // 날짜를 "yyyy.MM.dd HH:mm" 형식으로 변환하기 위한 포맷터
    val dateFormatter = remember {
        SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 제목과 작성 시간
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    // Date → String 변환하여 전달
                    text = dateFormatter.format(post.createdAt),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 좋아요·댓글 수 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { postViewModel.incrementLike(post.id) }) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "좋아요"
                    )
                }
                Text(
                    text = "${post.likeCount}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    imageVector = Icons.Filled.Comment,
                    contentDescription = "댓글",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${post.commentCount}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
