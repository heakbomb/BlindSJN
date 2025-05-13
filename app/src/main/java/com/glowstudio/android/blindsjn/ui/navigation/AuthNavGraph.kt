package com.glowstudio.android.blindsjn.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.glowstudio.android.blindsjn.feature.login.view.LoginScreen
import com.glowstudio.android.blindsjn.feature.login.view.SignupScreen

/**
 * 인증 관련 화면을 위한 네비게이션 그래프를 설정합니다.
 *
 * 이 그래프는 "login"을 시작 지점으로 하며, 로그인, 회원가입, 비밀번호 찾기 화면 간의 이동을 정의합니다.
 * 로그인 또는 회원가입이 성공하면 `onAuthSuccess` 콜백이 호출됩니다.
 *
 * @param onAuthSuccess 인증이 성공적으로 완료될 때 호출되는 콜백 함수입니다.
 */
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onAuthSuccess: () -> Unit
) {
    navigation(
        startDestination = "login",
        route = "auth"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { success ->
                    if (success) onAuthSuccess()
                },
                onSignupClick = { navController.navigate("signup") },
                onForgotPasswordClick = { navController.navigate("forgot") }
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupClick = { phoneNumber, password ->
                    onAuthSuccess()
                },
                onBackToLoginClick = { navController.navigateUp() }
            )
        }

        composable("forgot") {
            // TODO: ForgotPasswordScreen 구현
        }
    }
} 