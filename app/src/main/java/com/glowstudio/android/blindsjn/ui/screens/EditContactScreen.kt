package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton

/**
 * 연락처 정보를 수정할 수 있는 UI 화면을 표시합니다.
 *
 * 전화번호, 이메일, 주소를 입력받아 수정할 수 있는 입력 필드와 저장 버튼을 제공합니다.
 *
 * @param onBackClick 뒤로 가기 동작을 처리하는 콜백입니다.
 * @param onSave 저장 버튼 클릭 시 호출되는 콜백입니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactScreen(
    onBackClick: () -> Unit,
    onSave: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 전화번호 입력
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("전화번호") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        // 이메일 입력
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        // 주소 입력
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("주소") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            maxLines = 2
        )

        Spacer(modifier = Modifier.weight(1f))

        // 저장 버튼
        CommonButton(
            text = "저장",
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditContactScreenPreview() {
    EditContactScreen(
        onBackClick = { },
        onSave = { }
    )
}

