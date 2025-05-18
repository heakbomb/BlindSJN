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
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import com.glowstudio.android.blindsjn.ui.theme.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.AccountCircle


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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        post?.let {
            // 카드 형식 없이 기존 방식으로 UI 구성
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = DividerGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("익명", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text(it.time, color = DividerGray, style = MaterialTheme.typography.bodySmall)
                }
                OutlinedButton(
                    onClick = { isLiked = !isLiked },
                    border = ButtonDefaults.outlinedButtonBorder,
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        Icons.Default.ThumbUp,
                        contentDescription = null,
                        tint = if (isLiked) Error else DividerGray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "공감",
                        color = if (isLiked) Error else DividerGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(20.dp))
            // 좋아요/댓글 숫자
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ThumbUp, contentDescription = null, tint = Error, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text(it.likeCount.toString(), color = Error, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = ChatBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text(it.commentCount.toString(), color = ChatBlue, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider(
                color = DividerGray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("게시글을 불러오는 중입니다...")
            }
        }
        // Comments
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(comments) { comment ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = DividerGray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("익명", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = DividerGray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = DividerGray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = DividerGray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = comment.content,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = comment.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = DividerGray
                    )
                }
            }
        }
        // 댓글 입력창 (하단 고정 아님)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = ChatBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = newComment,
                onValueChange = { newComment = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("댓글을 입력하세요.") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (newComment.isNotBlank()) {
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
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newComment.isNotBlank()) {
                        commentViewModel.saveComment(
                            postId = safePostId,
                            userId = 1,
                            content = newComment
                        )
                        newComment = ""
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
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text("익명")
            }
        }
    }
}

@Composable
fun PostDetailCard(
    post: Post,
    isLiked: Boolean = false,
    onLikeClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // 상단 프로필/날짜
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = DividerGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("익명", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text(post.time, color = DividerGray, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // 본문
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(20.dp))
            // 좋아요/댓글 숫자
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ThumbUp, contentDescription = null, tint = Error, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text(post.likeCount.toString(), color = Error, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = ChatBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text(post.commentCount.toString(), color = ChatBlue, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            // 버튼
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = CardWhite,
                shadowElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ThumbUp,
                        contentDescription = null,
                        tint = DividerGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("공감", color = DividerGray, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
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
