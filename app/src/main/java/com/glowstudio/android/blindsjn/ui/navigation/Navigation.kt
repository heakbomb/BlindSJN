package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.glowstudio.android.blindsjn.feature.main.view.MainScreen
import com.glowstudio.android.blindsjn.feature.login.view.LoginScreen
import com.glowstudio.android.blindsjn.feature.login.view.SignupScreen
// import com.glowstudio.android.blindsjn.feature.forgot.view.ForgotPasswordScreen

/**
 * 앱의 주요 네비게이션 그래프를 설정하는 Composable 함수입니다.
 *
 * 로그인, 회원가입, 메인 화면 간의 네비게이션을 관리하며, 각 화면에서 발생하는 콜백에 따라 적절한 화면으로 이동합니다.
 *
 * @param navController 네비게이션을 제어하는 NavHostController 인스턴스
 */
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { success -> 
                    if (success) {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onSignupClick = { navController.navigate("signup") },
                onForgotPasswordClick = { navController.navigate("forgot") }
            )
        }
        composable("signup") {
            SignupScreen(
                onSignupClick = { _, _ -> 
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToLoginClick = { navController.navigateUp() }
            )
        }
/*
        // 비밀번호 찾기 등 인증 관련 route만 추가
        composable("forgot") {
            ForgotPasswordScreen(
                onBackToLoginClick = { navController.navigateUp() }
            )
        }
*/
        // 메인 앱 진입점
        composable("main") {
            MainScreen()
        }
    }
}
