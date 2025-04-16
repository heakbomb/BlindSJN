package com.glowstudio.android.blindsjn.ui.screens

/**
 * 로그인 스크린 로직
 *
 * TODO: 자동로그인 체크 박스
 **/

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.model.LoginRequest
import com.glowstudio.android.blindsjn.network.InternalServer
import com.glowstudio.android.blindsjn.network.AuthRepository
import com.glowstudio.android.blindsjn.R
import com.glowstudio.android.blindsjn.network.AutoLoginManager
import com.glowstudio.android.blindsjn.network.isNetworkAvailable
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.glowstudio.android.blindsjn.ui.components.*

// 로그인 함수 (서버 통신)
suspend fun login(phoneNumber: String, password: String): Boolean {
    val request = LoginRequest(phoneNumber, password)
    val response = InternalServer.api.login(request)

    return if (response.isSuccessful) {
        val result = response.body()
        Log.d("LoginScreen", "Login result: $result")
        result?.status == "success"
    } else {
        Log.e("LoginScreen", "Error: ${response.errorBody()?.string()}")
        false
    }
}

@Composable
fun LoginScreen(
    onLoginClick: (Boolean) -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isTestMode: Boolean = true
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var autoLoginEnabled by remember { mutableStateOf(false) }
    var showEmptyFieldsPopup by remember { mutableStateOf(false) }
    var showInvalidCredentialsPopup by remember { mutableStateOf(false) }
    var showNetworkErrorPopup by remember { mutableStateOf(false) }

    // 네트워크 상태 확인
    LaunchedEffect(Unit) {
        if (!isNetworkAvailable(context)) {
            showNetworkErrorPopup = true // 네트워크 오류 팝업 활성화
        } else {
            // 자동 로그인 로직
            autoLoginEnabled = AutoLoginManager.isAutoLoginEnabled(context)
            if (autoLoginEnabled) {
                AutoLoginManager.getSavedCredentials(context)?.let { (savedPhone, savedPassword) ->
                    phoneNumber = savedPhone
                    password = savedPassword
                    coroutineScope.launch {
                        val success = login(savedPhone, savedPassword)
                        if (success) onLoginClick(true)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally  // 중앙 정렬로 변경
    ) {
        // 상단 이미지
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)  // 높이 유지
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_image),
                contentDescription = "Login Image",
                contentScale = ContentScale.FillBounds,  // 화면 채우기 유지
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 환영 메시지
        Text(
            text = "어서오세요, 사장님!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold  // Bold 처리 유지
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 입력 필드들을 담을 Column
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 전화번호 입력
            CommonTextField(
                value = phoneNumber,
                onValueChange = { input -> phoneNumber = input.filter { it.isDigit() } },
                label = "전화번호",
                placeholder = "전화번호를 입력하세요",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 비밀번호 입력
            CommonTextField(
                value = password,
                onValueChange = { password = it },
                label = "비밀번호",
                placeholder = "비밀번호를 입력하세요",
                isPassword = true
            )

            // 자동 로그인과 비밀번호 찾기
            AutoLoginRow(
                autoLoginEnabled = autoLoginEnabled,
                onAutoLoginChange = { autoLoginEnabled = it },
                onForgotPasswordClick = onForgotPasswordClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 로그인 버튼
            CommonButton(
                text = "로그인",
                onClick = {
                    coroutineScope.launch {
                        if (phoneNumber.isEmpty() || password.isEmpty()) {
                            showEmptyFieldsPopup = true
                        } else if (isNetworkAvailable(context)) {
                            val success = AuthRepository.login(context, phoneNumber, password)
                            if (success) {
                                AutoLoginManager.saveLoginInfo(context, phoneNumber, password, autoLoginEnabled)
                                onLoginClick(true)
                            } else {
                                showInvalidCredentialsPopup = true
                            }
                        } else {
                            showNetworkErrorPopup = true
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회원가입 안내
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "계정이 없으신가요? ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(
                onClick = onSignupClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "회원가입",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 팝업들
        if (showEmptyFieldsPopup) {
            AlertDialog(
                onDismissRequest = { showEmptyFieldsPopup = false },
                title = { Text("입력 오류") },
                text = { Text("전화번호와 비밀번호를 입력해주세요.") },
                confirmButton = {
                    TextButton(onClick = { showEmptyFieldsPopup = false }) {
                        Text("확인")
                    }
                }
            )
        }

        if (showInvalidCredentialsPopup) {
            AlertDialog(
                onDismissRequest = { showInvalidCredentialsPopup = false },
                title = { Text("로그인 실패") },
                text = { Text("전화번호 또는 비밀번호가 올바르지 않습니다.") },
                confirmButton = {
                    TextButton(onClick = { showInvalidCredentialsPopup = false }) {
                        Text("확인")
                    }
                }
            )
        }

        // 네트워크 오류 팝업
        if (showNetworkErrorPopup) {
            AlertDialog(
                onDismissRequest = { /* 사용자가 팝업을 닫아도 무시 */ },
                title = { Text("네트워크 오류") },
                text = { Text("인터넷 연결이 필요합니다. 연결 상태를 확인해주세요.") },
                confirmButton = {
                    TextButton(onClick = {
                        showNetworkErrorPopup = false
                    }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginClick={},
        onSignupClick = {},
        onForgotPasswordClick= {},
        isTestMode = false
    )
}