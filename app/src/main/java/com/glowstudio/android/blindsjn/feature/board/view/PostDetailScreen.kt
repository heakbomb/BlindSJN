package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.feature.board.model.Comment
import com.glowstudio.android.blindsjn.feature.board.viewmodel.*
import com.glowstudio.android.blindsjn.ui.components.CommonButton
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import com.glowstudio.android.blindsjn.ui.theme.*

@Composable
fun PostDetailScreen(navController: NavController, postId: String) {
    val postViewModel: PostViewModel = viewModel()
    val commentViewModel: CommentViewModel = viewModel()
    val post by postViewModel.selectedPost.collectAsState()
    val comments by commentViewModel.comments.collectAsState()
    var newComment by remember { mutableStateOf("") }
    var isLiked by remember { mutableStateOf(false) }

    val safePostId = postId.toIntOrNull() ?: 1

    LaunchedEffect(safePostId) {
        postViewModel.loadPostById(safePostId)
        commentViewModel.loadComments(safePostId)
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
                            modifier = Modifier.clickable { isLiked = !isLiked }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ThumbUp,
                                contentDescription = "좋아요",
                                tint = if (isLiked) Color.Red else Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${it.likeCount + if (isLiked) 1 else 0}",
                                color = if (isLiked) Color.Red else Color.Gray,
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
                                "${it.commentCount}",
                                color = ChatBlue,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("댓글", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        LazyColumn {
                            items(comments) { comment ->
                                CommentItem(comment)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        OutlinedTextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            modifier = Modifier
                                .fillMaxWidth(0.9f),
                            placeholder = { Text("댓글을 입력하세요...") },
                            singleLine = false,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (!newComment.isNullOrBlank()) {
                                        commentViewModel.saveComment(
                                            postId = safePostId,
                                            userId = 1,
                                            content = newComment
                                        )
                                        newComment = ""
                                    }
                                }
                            )
                        )

                        CommonButton(
                            text = "등록",
                            onClick = {
                                if (newComment.isNotBlank()) {
                                    commentViewModel.saveComment(
                                        postId = safePostId,
                                        userId = 1,
                                        content = newComment
                                    )
                                    newComment = ""
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
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
        Text(
            text = comment.content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
