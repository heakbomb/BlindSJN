package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.glowstudio.android.blindsjn.ui.MainScreen
import com.glowstudio.android.blindsjn.ui.screens.*
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarState
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarViewModel
import com.glowstudio.android.blindsjn.model.Article
import com.google.gson.Gson
import java.net.URLDecoder
import androidx.compose.material.Text

@Composable
fun AppNavHost(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { success ->
                    if (success) navController.navigate("main")
                },
                onSignupClick = { navController.navigate("signup") },
                onForgotPasswordClick = { navController.navigate("forgot") }
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupClick = { phoneNumber, password ->
                    navController.navigate("main")
                },
                onBackToLoginClick = { navController.navigateUp() }
            )
        }

        composable("main") {
            MainScreen()
        }

        composable("newsDetail/{articleJson}") { backStackEntry ->
            val articleJson = backStackEntry.arguments?.getString("articleJson")
            val article = try {
                Gson().fromJson(URLDecoder.decode(articleJson, "UTF-8"), Article::class.java)
            } catch (e: Exception) {
                null
            }

            topBarViewModel.updateState(TopBarState("뉴스 상세", true, false))

            if (article != null) {
                NewsDetailScreen(
                    title = article.title ?: "제목 없음",
                    content = article.content,
                    description = article.description,
                    imageUrl = article.urlToImage
                )
            } else {
                Text("기사를 불러오는 데 실패했습니다.")
            }
        }

        composable("home") {
            topBarViewModel.updateState(TopBarState("홈 화면", false, false))
            HomeScreen(navController = navController)
        }

        composable("board") {
            topBarViewModel.updateState(TopBarState("게시판 목록", false, true))
            BoardScreen(navController = navController)
        }

        composable("boardDetail/{title}") { backStackEntry ->
            val postTitle = backStackEntry.arguments?.getString("title") ?: "게시글"
            topBarViewModel.updateState(TopBarState(postTitle, true, true))
            BoardDetailScreen(navController = navController, title = postTitle)
        }

        composable("writePost") {
            topBarViewModel.updateState(TopBarState("게시글 작성", true, false))
            WritePostScreen(navController = navController)
        }

        composable("postDetail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: "1"
            topBarViewModel.updateState(TopBarState("게시글 상세", true, false))
            PostDetailScreen(navController = navController, postId = postId)
        }

        composable("popular") {
            topBarViewModel.updateState(TopBarState("인기글", false, false))
            PopularScreen()
        }

        composable("message") {
            topBarViewModel.updateState(TopBarState("캘린더", false, true))
            MessageScreen(navController = navController)
        }

        composable("addSchedule") {
            topBarViewModel.updateState(TopBarState("일정 추가", true, false))
            AddScheduleScreen(
                onCancel = { navController.navigateUp() },
                onSave = { schedule ->
                    navController.navigateUp()
                }
            )
        }

        composable("certification") {
            topBarViewModel.updateState(TopBarState("사업자 인증", true, false))
            BusinessCertificationScreen(
                navController = navController,
                onConfirm = { phone, certNumber, industry ->
                    navController.navigate("someNextRoute")
                }
            )
        }

        composable("profile") {
            topBarViewModel.updateState(TopBarState("프로필", false, false))
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBusinessCertificationClick = { navController.navigate("certification") },
                onProfileEditClick = { navController.navigate("editProfile") },
                onContactEditClick = { navController.navigate("editContact") }
            )
        }

        composable("editProfile") {
            topBarViewModel.updateState(TopBarState("프로필 변경", true, false))
            EditProfileScreen(
                onBackClick = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }

        composable("editContact") {
            topBarViewModel.updateState(TopBarState("연락처 변경", true, false))
            EditContactScreen(
                onBackClick = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }

        // 푸드코스트 계산 메인 화면
        composable("foodCost") {
            topBarViewModel.updateState(TopBarState("푸드코스트 계산", true, false))
            FoodCostScreen(
                onRegisterRecipeClick = { navController.navigate("registerRecipe") },
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
