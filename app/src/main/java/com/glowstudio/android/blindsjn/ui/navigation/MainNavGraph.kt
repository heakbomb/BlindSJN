package com.glowstudio.android.blindsjn.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.glowstudio.android.blindsjn.feature.board.view.BoardDetailScreen
import com.glowstudio.android.blindsjn.feature.board.view.BoardScreen
import com.glowstudio.android.blindsjn.feature.board.view.WritePostScreen
import com.glowstudio.android.blindsjn.ui.screens.*
import com.glowstudio.android.blindsjn.feature.main.viewmodel.TopBarViewModel
import com.glowstudio.android.blindsjn.data.model.Article
import com.glowstudio.android.blindsjn.feature.board.view.PostDetailScreen
import com.glowstudio.android.blindsjn.feature.calendar.MessageScreen
import com.glowstudio.android.blindsjn.feature.certification.BusinessCertificationScreen
import com.glowstudio.android.blindsjn.feature.home.HomeScreen
import com.glowstudio.android.blindsjn.feature.home.NewsDetailScreen
import com.glowstudio.android.blindsjn.feature.popular.PopularScreen
import com.glowstudio.android.blindsjn.feature.profile.ProfileScreen
import com.google.gson.Gson
import java.net.URLDecoder

/**
 * 메인 네비게이션 그래프를 정의하여 홈, 뉴스 상세, 게시판, 인기글, 메시지, 프로필 등 주요 화면의 라우팅을 구성합니다.
 *
 * 홈 화면과 뉴스 상세 화면을 포함하며, 게시판, 인기글, 메시지, 프로필 관련 하위 네비게이션 그래프를 통합합니다.
 * 각 화면 진입 시 상단 바 상태를 적절히 설정합니다.
 */
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
            topBarViewModel.setMainBar()
            HomeScreen(navController = navController)
        }

        composable("news_detail/{articleJson}") { backStackEntry ->
            val articleJson = backStackEntry.arguments?.getString("articleJson")
            val article = try {
                Gson().fromJson(URLDecoder.decode(articleJson, "UTF-8"), Article::class.java)
            } catch (e: Exception) {
                null
            }

            topBarViewModel.setDetailBar("뉴스 상세")

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

/**
 * 게시판 관련 화면들의 네비게이션 그래프를 정의합니다.
 *
 * 게시판 목록, 게시글 상세, 게시글 작성, 게시글 상세(아이디 기반) 화면을 포함하며,
 * 각 화면 진입 시 상단 바 상태를 적절히 설정합니다.
 */
fun NavGraphBuilder.boardNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "board_list_screen",
        route = "board_root"
    ) {
        composable("board_list_screen") {
            topBarViewModel.setMainBar()
            BoardScreen(navController = navController)
        }

        
        composable("board_detail/{title}") { backStackEntry ->
            val postTitle = backStackEntry.arguments?.getString("title") ?: "게시글"
            topBarViewModel.setDetailBar("게시판 상세")
            BoardDetailScreen(navController = navController, title = postTitle)
        }

        composable("write_post_screen") {
            topBarViewModel.setDetailBar("게시글 작성")
            WritePostScreen(navController = navController)
        }

        composable("post_detail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: "1"
            topBarViewModel.setDetailBar("게시글 상세")
            PostDetailScreen(navController = navController, postId = postId)
        }
    }
}

/**
 * 인기 게시글 목록 화면을 포함하는 네비게이션 그래프를 정의합니다.
 *
 * "popular_list_screen"을 시작 지점으로 하며, 해당 화면에서 메인 상단 바를 설정하고 인기 게시글 목록을 표시합니다.
 */
fun NavGraphBuilder.popularNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "popular_list_screen",
        route = "popular_root"
    ) {
        composable("popular_list_screen") {
            topBarViewModel.setMainBar()
            PopularScreen()
        }
    }
}

/**
 * 메시지 및 일정 관련 화면의 내비게이션 그래프를 정의합니다.
 *
 * "calendar_screen"에서 메시지(일정) 화면을 시작하며, "add_schedule_screen"에서 일정 추가 화면을 제공합니다.
 * 각 화면 진입 시 상단 바 상태를 적절히 설정합니다.
 */
fun NavGraphBuilder.messageNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "calendar_screen",
        route = "message_root"
    ) {
        composable("calendar_screen") {
            topBarViewModel.setMainBar()
            MessageScreen(navController = navController)
        }

        composable("add_schedule_screen") {
            topBarViewModel.setDetailBar("일정 추가")
            AddScheduleScreen(
                onCancel = { navController.navigateUp() },
                onSave = { schedule ->
                    navController.navigateUp()
                }
            )
        }
    }
}

/**
 * 프로필 관련 화면의 네비게이션 그래프를 정의합니다.
 *
 * 프로필 메인, 사업자 인증, 프로필 수정, 연락처 수정 화면을 포함하며,
 * 각 화면 진입 시 상단 바 상태를 적절히 설정하고, 화면 간 이동 및 콜백 처리를 담당합니다.
 */
fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "profile_main_screen",
        route = "profile_root"
    ) {
        composable("profile_main_screen") {
            topBarViewModel.setMainBar()
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
            topBarViewModel.setDetailBar("사업자 인증")
            BusinessCertificationScreen(
                navController = navController,
                onConfirm = { phone, certNumber, industry ->
                    navController.navigate("someNextRoute")
                }
            )
        }

        composable("edit_profile_screen") {
            topBarViewModel.setDetailBar("프로필 변경")
            EditProfileScreen(
                onBackClick = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }

        composable("edit_contact_screen") {
            topBarViewModel.setDetailBar("연락처 변경")
            EditContactScreen(
                onBackClick = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }
    }
} 