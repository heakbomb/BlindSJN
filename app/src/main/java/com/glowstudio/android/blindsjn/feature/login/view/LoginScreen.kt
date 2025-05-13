package com.glowstudio.android.blindsjn.feature.login.view

/**
 * 로그인 스크린 로직
 *
 * TODO: 자동로그인 체크 박스
 **/

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.data.model.LoginRequest
import com.glowstudio.android.blindsjn.data.network.InternalServer
import com.glowstudio.android.blindsjn.R
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.feature.login.LoginViewModel
import com.glowstudio.android.blindsjn.ui.components.common.AutoLoginRow
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton
import com.glowstudio.android.blindsjn.ui.components.common.CommonTextField

// 로그인 함수 (서버 통신)
/**
 * 서버에 전화번호와 비밀번호로 로그인 요청을 수행합니다.
 *
 * @param phoneNumber 로그인에 사용할 전화번호
 * @param password 로그인에 사용할 비밀번호
 * @return 로그인 성공 시 true, 실패 시 false를 반환합니다.
 */
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

/**
 * 로그인 화면 UI를 표시하는 컴포저블 함수입니다.
 *
 * 로그인, 회원가입, 비밀번호 찾기 등 인증 관련 인터페이스와 입력 필드를 제공하며,
 * 로그인 성공, 입력 오류, 인증 실패, 네트워크 오류 등 다양한 상태에 따라 팝업을 표시합니다.
 *
 * @param onLoginClick 로그인 성공 시 호출되는 콜백. 성공 여부를 인자로 전달합니다.
 * @param onSignupClick 회원가입 화면으로 이동할 때 호출되는 콜백.
 * @param onForgotPasswordClick 비밀번호 찾기 화면으로 이동할 때 호출되는 콜백.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginClick: (Boolean) -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // onLoginSuccess 콜백 설정
    LaunchedEffect(Unit) {
        viewModel.setOnLoginSuccess { success ->
            if (success) {
                onLoginClick(true)
            }
        }
    }

    // 자동 로그인 체크
    LaunchedEffect(Unit) {
        viewModel.checkAutoLogin(context)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 이미지
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_image),
                contentDescription = "Login Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 환영 메시지
        Text(
            text = "어서오세요, 사장님!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
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
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 전화번호 입력
            CommonTextField(
                value = uiState.phoneNumber,
                onValueChange = { viewModel.updatePhoneNumber(it) },
                label = "전화번호",
                placeholder = "전화번호를 입력하세요",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 비밀번호 입력
            CommonTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = "비밀번호",
                placeholder = "비밀번호를 입력하세요",
                isPassword = true
            )

            // 자동 로그인과 비밀번호 찾기
            AutoLoginRow(
                autoLoginEnabled = uiState.autoLoginEnabled,
                onAutoLoginChange = { viewModel.updateAutoLogin(it) },
                onForgotPasswordClick = onForgotPasswordClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 로그인 버튼
            CommonButton(
                text = "로그인",
                onClick = {
                    viewModel.login(context, uiState.phoneNumber, uiState.password)
                },
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading
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
        if (uiState.showEmptyFieldsPopup) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissEmptyFieldsPopup() },
                title = { Text("입력 오류") },
                text = { Text("전화번호와 비밀번호를 입력해주세요.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissEmptyFieldsPopup() }) {
                        Text("확인")
                    }
                }
            )
        }

        if (uiState.showInvalidCredentialsPopup) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissInvalidCredentialsPopup() },
                title = { Text("로그인 실패") },
                text = { Text("전화번호 또는 비밀번호가 올바르지 않습니다.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissInvalidCredentialsPopup() }) {
                        Text("확인")
                    }
                }
            )
        }

        if (uiState.showNetworkErrorPopup) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissNetworkErrorPopup() },
                title = { Text("네트워크 오류") },
                text = { Text("인터넷 연결이 필요합니다. 연결 상태를 확인해주세요.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissNetworkErrorPopup() }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

/**
 * 로그인 화면의 미리보기를 표시하는 Compose 프리뷰 함수입니다.
 *
 * 실제 동작 없이 기본 콜백을 사용하여 로그인 UI의 디자인을 미리 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginClick = {},
        onSignupClick = {},
        onForgotPasswordClick = {}
    )
}