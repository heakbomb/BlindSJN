package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.model.Comment
import com.glowstudio.android.blindsjn.ui.viewModel.PostViewModel
import com.glowstudio.android.blindsjn.ui.components.CommonButton

@Composable
fun PostDetailScreen(navController: NavController, postId: String) {
    val viewModel: PostViewModel = viewModel()
    val post by viewModel.selectedPost.collectAsState()
    val comments by viewModel.comments.collectAsState()
    var newComment by remember { mutableStateOf("") }
    var isLiked by remember { mutableStateOf(false) }

    LaunchedEffect(postId) {
        viewModel.loadPostById(postId.toInt())
        viewModel.loadComments(postId.toInt())
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                post?.let {
                    Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it.content, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    // 좋아요 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                isLiked = !isLiked
                            }
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "좋아요"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${it.likeCount + if (isLiked) 1 else 0}")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 댓글 리스트
                    Text("댓글", style = MaterialTheme.typography.titleMedium)
                    LazyColumn {
                        items(comments) { comment ->
                            CommentItem(comment)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 댓글 입력
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("댓글을 입력하세요...") }
                        )
                        CommonButton(
                            text = "등록",
                            onClick = {
                                if (newComment.isNotBlank()) {
                                    viewModel.saveComment(
                                        postId = postId.toInt(),
                                        userId = 1, // 실제 사용자 ID로 교체 필요
                                        content = newComment
                                    )
                                    newComment = ""
                                }
                            }
                        )
                    }
                } ?: run {
                    Text("게시글을 불러오는 중입니다...", modifier = Modifier.padding(16.dp))
                }
            }
        }
    )
}

@Composable
fun CommentItem(comment: Comment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = "익명", style = MaterialTheme.typography.bodyMedium)
        //Text(text = comment.content, style = MaterialTheme.typography.bodySmall)
    }
}
