package com.glowstudio.android.blindsjn.feature.board

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.data.model.Post
import com.glowstudio.android.blindsjn.ui.viewModel.PostViewModel

@Composable
fun BoardDetailScreen(navController: NavController, title: String) {
    val viewModel: PostViewModel = viewModel()
    val posts by viewModel.posts.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    var selectedCategory by remember { mutableStateOf("모든 분야") }

    val categories = listOf("모든 분야", "카페", "식당", "배달 전문", "패스트푸드", "호텔")

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("writePost") },
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
                Text(
                    text = "❤️ $likeCount",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        if (!isLiked) {
                            likeCount++
                            isLiked = true
                            viewModel.incrementLike(post.id)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "💬 ${post.commentCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
