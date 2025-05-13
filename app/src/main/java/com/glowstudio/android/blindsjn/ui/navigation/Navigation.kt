// Navigation.kt
package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.glowstudio.android.blindsjn.model.Article
import com.glowstudio.android.blindsjn.model.BoardCategory
import com.glowstudio.android.blindsjn.ui.MainScreen
import com.glowstudio.android.blindsjn.ui.screens.*
import com.glowstudio.android.blindsjn.ui.viewModel.BoardViewModel
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarState
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarViewModel
import com.google.gson.Gson
import java.net.URLDecoder

@Composable
fun AppNavHost(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    NavHost(navController = navController, startDestination = "login") {

        // 로그인 화면
        composable("login") {
            LoginScreen(
                onLoginClick          = { ok  -> if (ok) navController.navigate("main") },
                onSignupClick         = {     navController.navigate("signup") },
                onForgotPasswordClick = {     navController.navigate("forgot") }
            )
        }

        // 회원가입 화면
        composable("signup") {
            SignupScreen(
                onSignupClick      = { _, _ -> navController.navigate("main") },
                onBackToLoginClick = {       navController.navigateUp() }
            )
        }

        // 메인 화면
        composable("main") {
            MainScreen()
        }

        // 뉴스 상세 화면
        composable("newsDetail/{articleJson}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("articleJson")
            val article = try {
                Gson().fromJson(URLDecoder.decode(json, "UTF-8"), Article::class.java)
            } catch (_: Exception) {
                null
            }
            topBarViewModel.updateState(TopBarState("뉴스 상세", true, false))
            NewsDetailScreen(
                title       = article?.title ?: "제목 없음",
                content     = article?.content.orEmpty(),
                description = article?.description.orEmpty(),
                imageUrl    = article?.urlToImage,
                link        = article?.link
            )
        }

        // 홈 화면
        composable("home") {
            topBarViewModel.updateState(TopBarState("홈 화면", false, false))
            HomeScreen(navController)
        }

        // 게시판 카테고리 선택 화면
        composable("boardCategory") {
            topBarViewModel.updateState(TopBarState("게시판", false, true))
            BoardCategoryScreen(navController)
        }

        // 업종 · 자유 · 질문 · 인기 게시판
        listOf(
            BoardCategory.INDUSTRY to "업종게시판",
            BoardCategory.FREE     to "자유게시판",
            BoardCategory.QUESTION to "질문게시판",
            BoardCategory.POPULAR  to "인기게시판"
        ).forEach { (category, title) ->
            composable(category.route) {
                topBarViewModel.updateState(TopBarState(title, true, true))
                val vm: BoardViewModel = viewModel()
                val posts by vm.posts.collectAsState()
                val user  by vm.user.collectAsState()
                BoardListScreen(navController, category, posts, user)
            }
        }

        // 글쓰기 화면 (카테고리 정보 필수 전달)
        composable("writePost/{category}") { backStackEntry ->
            topBarViewModel.updateState(TopBarState("게시글 작성", true, false))
            val catRoute = backStackEntry.arguments?.getString("category")
                ?: BoardCategory.FREE.route
            val category = BoardCategory.values()
                .find { it.route == catRoute }
                ?: BoardCategory.FREE

            WritePostScreen(navController, category)
        }

        // 게시글 상세 화면 (postId는 String 타입)
        composable("postDetail/{postId}") { backStackEntry ->
            topBarViewModel.updateState(TopBarState("게시글 상세", true, false))
            val postId = backStackEntry.arguments?.getString("postId")
                ?: return@composable
            // String 그대로 전달합니다.
            PostDetailScreen(navController, postId)
        }

        // 매출관리 화면
        composable("management") {
            topBarViewModel.updateState(TopBarState("매출관리", false, false))
            ManagementScreen()
        }

        // 캘린더 화면
        composable("message") {
            topBarViewModel.updateState(TopBarState("캘린더", false, true))
            MessageScreen(navController)
        }

        // 일정 추가 화면
        composable("addSchedule") {
            topBarViewModel.updateState(TopBarState("일정 추가", true, false))
            AddScheduleScreen(
                onCancel = { navController.navigateUp() },
                onSave   = { navController.navigateUp() }
            )
        }

        // 사업자 인증 화면
        composable("certification") {
            topBarViewModel.updateState(TopBarState("사업자 인증", true, false))
            BusinessCertificationScreen(
                navController = navController,
                onConfirm     = { _, _, _ -> navController.navigate("someNextRoute") }
            )
        }

        // 프로필 화면
        composable("profile") {
            topBarViewModel.updateState(TopBarState("프로필", false, false))
            ProfileScreen(
                onLogoutClick               = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBusinessCertificationClick = { navController.navigate("certification") },
                onProfileEditClick           = { navController.navigate("editProfile") },
                onContactEditClick           = { navController.navigate("editContact") }
            )
        }

        // 프로필 변경 화면
        composable("editProfile") {
            topBarViewModel.updateState(TopBarState("프로필 변경", true, false))
            EditProfileScreen(
                onBackClick = { navController.navigateUp() },
                onSave      = { navController.navigateUp() }
            )
        }

        // 연락처 변경 화면
        composable("editContact") {
            topBarViewModel.updateState(TopBarState("연락처 변경", true, false))
            EditContactScreen(
                onBackClick = { navController.navigateUp() },
                onSave      = { navController.navigateUp() }
            )
        }

        // 푸드코스트 계산 화면
        composable("foodCost") {
            topBarViewModel.updateState(TopBarState("푸드코스트 계산", true, false))
            FoodCostScreen(
                onRegisterRecipeClick     = { navController.navigate("registerRecipe") },
                onRegisterIngredientClick = { navController.navigate("registerIngredient") }
            )
        }

        // 레시피 등록 화면
        composable("registerRecipe") {
            topBarViewModel.updateState(TopBarState("레시피 등록", true, false))
            RegisterRecipeScreen()
        }

        // 재료 등록 화면
        composable("registerIngredient") {
            topBarViewModel.updateState(TopBarState("재료 등록", true, false))
            RegisterIngredientScreen()
        }
    }
}
