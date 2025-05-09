package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.feature.board.viewmodel.PostViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritePostScreen(navController: NavController) {
    val viewModel: PostViewModel = viewModel()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var isQuestion by remember { mutableStateOf(false) }

    val statusMessage by viewModel.statusMessage.collectAsState()

    LaunchedEffect(statusMessage) {
        if (!statusMessage.isNullOrEmpty() && 
            (statusMessage!!.contains("성공") || statusMessage!!.contains("저장"))) {
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("게시글 작성") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                PostForm(
                    title = title,
                    onTitleChange = { title = it },
                    content = content,
                    onContentChange = { content = it },
                    isAnonymous = isAnonymous,
                    onAnonymousChange = { isAnonymous = it },
                    isQuestion = isQuestion,
                    onQuestionChange = { isQuestion = it },
                    onSubmit = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            viewModel.savePost(title, content, 1, "카페")
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun PostForm(
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    isAnonymous: Boolean,
    onAnonymousChange: (Boolean) -> Unit,
    isQuestion: Boolean,
    onQuestionChange: (Boolean) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = { Text("내용") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            minLines = 5
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Checkbox(
                    checked = isAnonymous,
                    onCheckedChange = onAnonymousChange
                )
                Text("익명")
            }
            
            Row {
                Checkbox(
                    checked = isQuestion,
                    onCheckedChange = onQuestionChange
                )
                Text("질문")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("작성하기")
        }
    }
}
