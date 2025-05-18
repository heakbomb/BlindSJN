package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.feature.board.model.Comment
import com.glowstudio.android.blindsjn.feature.board.model.Post
import com.glowstudio.android.blindsjn.feature.board.viewmodel.*
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import com.glowstudio.android.blindsjn.ui.theme.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun PostDetailScreen(navController: NavController, postId: String) {
    val postViewModel: PostViewModel = viewModel()
    val commentViewModel: CommentViewModel = viewModel()
    val post by postViewModel.selectedPost.collectAsState()
    val comments by commentViewModel.comments.collectAsState()
    var newComment by remember { mutableStateOf("") }
    var isLiked by remember { mutableStateOf(false) }
    
    // 신고 관련 상태
    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }
    var showReportSuccessDialog by remember { mutableStateOf(false) }
    val reportResult by postViewModel.reportResult.collectAsState()

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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
                        IconButton(onClick = { showReportDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.Report,
                                contentDescription = "신고",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

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

                    // 신고 다이얼로그
                    if (showReportDialog) {
                        AlertDialog(
                            onDismissRequest = { showReportDialog = false },
                            title = { Text("게시글 신고") },
                            text = {
                                OutlinedTextField(
                                    value = reportReason,
                                    onValueChange = { reportReason = it },
                                    placeholder = { Text("신고 사유를 입력하세요") }
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        postViewModel.reportPost(postId = safePostId, userId = 1, reason = reportReason)
                                        showReportDialog = false
                                    }
                                ) { Text("신고하기") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showReportDialog = false }) { Text("취소") }
                            }
                        )
                    }

                    // 신고 결과 알림
                    reportResult?.let {
                        AlertDialog(
                            onDismissRequest = { postViewModel.clearReportResult() },
                            title = { Text("알림") },
                            text = { Text(it) },
                            confirmButton = {
                                TextButton(onClick = { postViewModel.clearReportResult() }) { Text("확인") }
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
        Text(
            text = comment.content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    BlindSJNTheme {
        val navController = rememberNavController()
        PostDetailScreen(navController = navController, postId = "1")
    }
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    BlindSJNTheme {
        CommentItem(
            comment = Comment(
                commentId = 1,
                postId = 1,
                userId = 1,
                content = "이것은 예시 댓글입니다. 댓글 내용이 길어지면 어떻게 보이는지 확인하기 위한 텍스트입니다.",
                createdAt = "2024-03-20"
            )
        )
    }
}
