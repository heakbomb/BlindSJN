package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.glowstudio.android.blindsjn.ui.screens.*
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarState
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarViewModel
import com.glowstudio.android.blindsjn.model.Article
import com.google.gson.Gson
import java.net.URLDecoder

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "home_screen",
        route = "main_nav"
    ) {
        // 홈 화면
        composable("home_screen") {
            topBarViewModel.updateState(TopBarState("홈 화면", false, false))
            HomeScreen(navController = navController)
        }

        composable("news_detail/{articleJson}") { backStackEntry ->
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
            }
        }
        
        // 게시판 네비게이션 그래프
        boardNavGraph(navController, topBarViewModel)
        
        // 인기글 네비게이션 그래프
        popularNavGraph(navController, topBarViewModel)
        
        // 메시지(캘린더) 네비게이션 그래프
        messageNavGraph(navController, topBarViewModel)
        
        // 프로필 네비게이션 그래프
        profileNavGraph(navController, topBarViewModel)
    }
}

fun NavGraphBuilder.boardNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "board_list_screen",
        route = "board_root"
    ) {
        composable("board_list_screen") {
            topBarViewModel.updateState(TopBarState("게시판 목록", false, true))
            BoardScreen(navController = navController)
        }

        
        composable("board_detail/{title}") { backStackEntry ->
            val postTitle = backStackEntry.arguments?.getString("title") ?: "게시글"
            topBarViewModel.updateState(TopBarState(postTitle, true, true))
            BoardDetailScreen(navController = navController, title = postTitle)
        }

        composable("write_post_screen") {
            topBarViewModel.updateState(TopBarState("게시글 작성", true, false))
            WritePostScreen(navController = navController)
        }

        composable("post_detail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: "1"
            topBarViewModel.updateState(TopBarState("게시글 상세", true, false))
            PostDetailScreen(navController = navController, postId = postId)
        }
    }
}

fun NavGraphBuilder.popularNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "popular_list_screen",
        route = "popular_root"
    ) {
        composable("popular_list_screen") {
            topBarViewModel.updateState(TopBarState("인기글", false, false))
            PopularScreen()
        }
    }
}

fun NavGraphBuilder.messageNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "calendar_screen",
        route = "message_root"
    ) {
        composable("calendar_screen") {
            topBarViewModel.updateState(TopBarState("캘린더", false, true))
            MessageScreen(navController = navController)
        }

        composable("add_schedule_screen") {
            topBarViewModel.updateState(TopBarState("일정 추가", true, false))
            AddScheduleScreen(
                onCancel = { navController.navigateUp() },
                onSave = { schedule ->
                    navController.navigateUp()
                }
            )
        }
    }
}

fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "profile_main_screen",
        route = "profile_root"
    ) {
        composable("profile_main_screen") {
            topBarViewModel.updateState(TopBarState("프로필", false, false))
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate("auth") {
                        popUpTo("main_root") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBusinessCertificationClick = { navController.navigate("certification_screen") },
                onProfileEditClick = { navController.navigate("edit_profile_screen") },
                onContactEditClick = { navController.navigate("edit_contact_screen") }
            )
        }

        composable("certification_screen") {
            topBarViewModel.updateState(TopBarState("사업자 인증", true, false))
            BusinessCertificationScreen(
                navController = navController,
                onConfirm = { phone, certNumber, industry ->
                    navController.navigate("someNextRoute")
                }
            )
        }

        composable("edit_profile_screen") {
            topBarViewModel.updateState(TopBarState("프로필 변경", true, false))
            EditProfileScreen(
                onBackClick = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }

        composable("edit_contact_screen") {
            topBarViewModel.updateState(TopBarState("연락처 변경", true, false))
            EditContactScreen(
                onBackClick = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }
    }
} 