package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.ui.components.CommonButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSave: () -> Unit
) {
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var introduction by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 프로필 이미지 변경 섹션
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            ) {
                // 이미지 선택 버튼
                Button(
                    onClick = { /* 이미지 선택 로직 */ },
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape
                ) {
                    Text("이미지 변경")
                }
            }
        }

        // 닉네임 입력
        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("닉네임") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        // 자기소개 입력
        OutlinedTextField(
            value = introduction,
            onValueChange = { introduction = it },
            label = { Text("자기소개") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(vertical = 8.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.weight(1f))

        // 저장 버튼
        CommonButton(
            text = "저장",
            onClick = onSave
        )

        CommonButton(
            text = "취소",
            onClick = onBackClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        onBackClick = { },
        onSave = { }
    )
}

