package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.model.Comment
import com.glowstudio.android.blindsjn.ui.components.CommonButton
import com.glowstudio.android.blindsjn.ui.viewModel.PostViewModel

@Composable
fun PostDetailScreen(navController: NavController, postId: String) {
    val viewModel: PostViewModel = viewModel()
    val post by viewModel.selectedPost.collectAsState()
    val comments by viewModel.comments.collectAsState()
    var newComment by remember { mutableStateOf("") }
    var isLiked by remember { mutableStateOf(false) }
    var isEditingComment by remember { mutableStateOf<Comment?>(null) }

    val safePostId = postId.toIntOrNull() ?: 1

    LaunchedEffect(safePostId) {
        viewModel.loadPostById(safePostId)
        viewModel.loadComments(safePostId)
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                isLiked = !isLiked
                                viewModel.incrementLike(safePostId)
                            }
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "좋아요",
                                tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${it.likeCount + if (isLiked) 1 else 0}")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("댓글", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(comments) { comment ->
                            CommentItem(
                                comment = comment,
                                isAuthor = comment.userId == 1,
                                onEdit = {
                                    isEditingComment = comment
                                    newComment = comment.content
                                },
                                onDelete = {
                                    viewModel.deleteComment(comment.id, safePostId)
                                }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            modifier = Modifier.fillMaxWidth(0.9f),
                            placeholder = { Text(if (isEditingComment != null) "댓글을 수정하세요..." else "댓글을 입력하세요...") },
                            singleLine = false
                        )

                        CommonButton(
                            text = if (isEditingComment != null) "수정" else "등록",
                            onClick = {
                                if (newComment.isNotBlank()) {
                                    if (isEditingComment != null) {
                                        viewModel.editComment(
                                            commentId = isEditingComment!!.id,
                                            content = newComment,
                                            postId = safePostId
                                        )
                                        isEditingComment = null
                                    } else {
                                        viewModel.saveComment(
                                            postId = safePostId,
                                            userId = 1,
                                            content = newComment
                                        )
                                    }
                                    newComment = ""
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CommentItem(
    comment: Comment,
    isAuthor: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "익명", style = MaterialTheme.typography.bodyMedium)

            if (isAuthor) {
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "수정")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "삭제")
                    }
                }
            }
        }

        Text(
            text = comment.content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}