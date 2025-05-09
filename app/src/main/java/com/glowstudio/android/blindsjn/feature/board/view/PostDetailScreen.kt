package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.feature.board.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(navController: NavController, postId: String) {
    val viewModel: PostViewModel = viewModel()
    val post by viewModel.selectedPost.collectAsState()
    val comments by viewModel.comments.collectAsState()
    var newComment by remember { mutableStateOf("") }
    var isLiked by remember { mutableStateOf(false) }

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
                    PostContent(
                        post = it,
                        isLiked = isLiked,
                        onLikeClick = { isLiked = !isLiked }
                    )
                    
                    CommentSection(
                        comments = comments,
                        newComment = newComment,
                        onNewCommentChange = { newComment = it },
                        onCommentSubmit = {
                            viewModel.saveComment(safePostId, 1, newComment)
                            newComment = ""
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun PostContent(
    post: com.glowstudio.android.blindsjn.feature.board.model.Post,
    isLiked: Boolean,
    onLikeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${post.category} ${post.experience}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row {
                    Text(
                        text = "❤️ ${post.likeCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable(onClick = onLikeClick)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "💬 ${post.commentCount}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CommentSection(
    comments: List<com.glowstudio.android.blindsjn.feature.board.model.Comment>,
    newComment: String,
    onNewCommentChange: (String) -> Unit,
    onCommentSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "댓글",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        comments.forEach { comment ->
            CommentItem(comment = comment)
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            TextField(
                value = newComment,
                onValueChange = onNewCommentChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("댓글을 입력하세요") }
            )
            Button(
                onClick = onCommentSubmit,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("작성")
            }
        }
    }
}

@Composable
fun CommentItem(comment: com.glowstudio.android.blindsjn.feature.board.model.Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "사용자 ${comment.userId}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
