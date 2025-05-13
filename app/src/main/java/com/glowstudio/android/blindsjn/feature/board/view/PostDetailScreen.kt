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

/**
 * 지정된 게시글의 상세 정보와 댓글 목록을 표시하는 Compose UI 화면입니다.
 *
 * 게시글의 제목, 내용, 좋아요 수, 댓글 수를 보여주며, 댓글 목록을 스크롤하여 확인할 수 있습니다.
 * 사용자는 댓글 입력란을 통해 새로운 댓글을 작성하고 등록할 수 있습니다.
 * 좋아요 버튼을 눌러 로컬 상태에서 좋아요 표시 및 수를 변경할 수 있습니다.
 *
 * @param navController 화면 간 내비게이션을 위한 컨트롤러
 * @param postId 상세 정보를 표시할 게시글의 식별자(문자열)
 */
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

/**
 * 댓글의 작성자와 내용을 표시하는 컴포저블입니다.
 *
 * @param comment 표시할 댓글 데이터
 */
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

/**
 * `PostDetailScreen`의 미리보기를 렌더링합니다.
 *
 * 샘플 postId("1")와 네비게이션 컨트롤러를 사용하여 상세 게시글 화면의 UI를 디자인 타임에 미리 볼 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    BlindSJNTheme {
        val navController = rememberNavController()
        PostDetailScreen(navController = navController, postId = "1")
    }
}

/**
 * `CommentItem` 컴포저블의 UI를 예시 댓글 데이터로 미리보기합니다.
 */
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
