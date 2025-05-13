package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.feature.board.viewmodel.PostViewModel
import com.glowstudio.android.blindsjn.feature.board.viewmodel.BoardViewModel
import com.glowstudio.android.blindsjn.feature.board.model.BoardCategory
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton
import com.glowstudio.android.blindsjn.feature.board.viewmodel.WritePostViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

/**
 * 게시글 작성 화면을 표시하는 Compose UI입니다.
 *
 * 사용자는 카테고리 선택, 제목 및 내용 입력, 질문 여부와 익명 여부 설정, 태그 지정(선택적) 기능을 이용해 게시글을 작성할 수 있습니다.
 * 제목 또는 내용이 비어 있을 경우 상태 메시지로 입력을 요청하며, 게시글 작성이 성공하면 이전 화면으로 이동합니다.
 *
 * @param navController 네비게이션 처리를 위한 NavController 인스턴스
 * @param tags 콤마(,)로 구분된 태그 문자열. 지정 시 해당 태그들이 선택된 상태로 설정됩니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritePostScreen(
    navController: NavController,
    tags: String? = null
) {
    val viewModel: PostViewModel = viewModel()
    val boardViewModel: BoardViewModel = viewModel()
    val writePostViewModel = remember { WritePostViewModel(boardViewModel) }
    val categories by writePostViewModel.categories.collectAsState()
    val selectedCategory by writePostViewModel.selectedCategory.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var isQuestion by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val contentFocusRequester = remember { FocusRequester() }
    val statusMessage by viewModel.statusMessage.collectAsState()

    LaunchedEffect(statusMessage) {
        statusMessage?.let { message ->
            if (message.contains("성공") || message.contains("저장")) {
                navController.navigateUp()
            }
        }
    }

    LaunchedEffect(tags) {
        tags?.let { tagString ->
            val tagList = tagString.split(",")
            writePostViewModel.setSelectedTags(tagList)
        }
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .clickable { expanded = true }
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedCategory.emoji,
                            style = MaterialTheme.typography.titleLarge
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.title) },
                                    onClick = {
                                        writePostViewModel.selectCategory(category)
                                        expanded = false
                                    },
                                    leadingIcon = { Text(category.emoji) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("제목을 입력해주세요.") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { contentFocusRequester.requestFocus() })
                    )
                }

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
                            viewModel.setStatusMessage("제목과 내용을 입력하세요.")
                        } else {
                            val userId = 1
                            val industry = "카페"
                            viewModel.savePost(title, content, userId, industry)
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                )

                statusMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    )
}

/**
 * `WritePostScreen`의 Compose 미리보기를 제공합니다.
 *
 * Compose Preview에서 글 작성 화면의 UI를 시각적으로 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun WritePostScreenPreview() {
    WritePostScreen(navController = rememberNavController())
}
