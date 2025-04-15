package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.ui.viewModel.PostViewModel
import com.glowstudio.android.blindsjn.ui.components.CommonButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritePostScreen(
    navController: NavController,
    postViewModel: PostViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var isQuestion by remember { mutableStateOf(false) }

    val contentFocusRequester = remember { FocusRequester() }
    val statusMessage by postViewModel.statusMessage.collectAsState()

    LaunchedEffect(statusMessage) {
        if (!statusMessage.isNullOrEmpty()) {
            if (statusMessage!!.contains("성공") || statusMessage!!.contains("저장")) {
                navController.navigateUp()
            }
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
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("제목을 입력해주세요.") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { contentFocusRequester.requestFocus() })
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("자유롭게 얘기해보세요.\n#질문 #고민") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .focusRequester(contentFocusRequester),
                    maxLines = Int.MAX_VALUE
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        IconButton(onClick = { /* 이미지 첨부 */ }) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "이미지 첨부")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { /* 파일 첨부 */ }) {
                            Icon(Icons.Default.AttachFile, contentDescription = "파일 첨부")
                        }
                    }

                    // 이전처럼 "질문"과 "익명" 체크박스를 오른쪽에
                    Row {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isQuestion = !isQuestion }
                        ) {
                            Checkbox(checked = isQuestion, onCheckedChange = null)
                            Text("질문")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isAnonymous = !isAnonymous }
                        ) {
                            Checkbox(checked = isAnonymous, onCheckedChange = null)
                            Text("익명")
                        }
                    }
                }

                CommonButton(
                    text = "작성",
                    onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            postViewModel.setStatusMessage("제목과 내용을 입력하세요.")
                        } else {
                            val userId = 1
                            val industry = "카페"
                            postViewModel.savePost(title, content, userId, industry)
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                )

                statusMessage?.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    )
}
