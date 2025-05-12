// WritePostScreen.kt
package com.glowstudio.android.blindsjn.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.model.BoardCategory
import com.glowstudio.android.blindsjn.ui.viewModel.BoardViewModel

@Composable
fun WritePostScreen(
    navController: NavController,
    category: BoardCategory
) {
    // ViewModel에서 사용자 정보 수집
    val vm: BoardViewModel = viewModel()
    val user by vm.user.collectAsState()

    // Composable 컨텍스트에서 Context를 미리 꺼냅니다
    val context = LocalContext.current

    // 업종게시판 접근 제한
    LaunchedEffect(category, user) {
        if (category == BoardCategory.INDUSTRY && user.certifiedIndustries.isEmpty()) {
            Toast.makeText(
                context,
                "사업자 인증 후 업종게시판을 이용할 수 있습니다.",
                Toast.LENGTH_LONG
            ).show()
            navController.popBackStack()
        }
    }

    // 입력 상태 변수
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    // 화면 레이아웃
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                // TODO: vm.createPost(PostRequest(...)) 등 실제 저장 로직 추가
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("등록")
        }
    }
}
