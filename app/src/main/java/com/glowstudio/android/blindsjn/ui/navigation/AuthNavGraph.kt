package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.glowstudio.android.blindsjn.ui.screens.LoginScreen
import com.glowstudio.android.blindsjn.ui.screens.SignupScreen

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