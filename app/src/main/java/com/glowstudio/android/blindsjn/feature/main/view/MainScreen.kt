package com.glowstudio.android.blindsjn.feature.main.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.navigation.AppNavHost
import com.glowstudio.android.blindsjn.feature.main.viewmodel.TopBarViewModel
import com.glowstudio.android.blindsjn.feature.main.viewmodel.NavigationViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.tooling.preview.Preview
import com.glowstudio.android.blindsjn.feature.main.view.TopBarMain
import com.glowstudio.android.blindsjn.feature.main.view.TopBarDetail
import com.glowstudio.android.blindsjn.feature.main.viewmodel.TopBarType
import com.glowstudio.android.blindsjn.feature.board.view.BoardScreen
import com.glowstudio.android.blindsjn.feature.board.view.BoardDetailScreen
import com.glowstudio.android.blindsjn.feature.board.view.WritePostScreen
import com.glowstudio.android.blindsjn.feature.board.view.PostDetailScreen
import com.glowstudio.android.blindsjn.feature.home.HomeScreen
import com.glowstudio.android.blindsjn.feature.profile.ProfileScreen
import com.glowstudio.android.blindsjn.feature.popular.PopularScreen
import com.glowstudio.android.blindsjn.feature.calendar.MessageScreen
import com.glowstudio.android.blindsjn.ui.screens.AddScheduleScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.net.URLEncoder
import com.google.gson.Gson
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import com.glowstudio.android.blindsjn.ui.components.banner.BannerSection
import com.glowstudio.android.blindsjn.ui.components.shortcut.ShortcutSection
import com.glowstudio.android.blindsjn.ui.components.news.NaverNewsSection
import com.glowstudio.android.blindsjn.ui.components.hotpost.HotPostsSection
import com.glowstudio.android.blindsjn.ui.components.sales.SalesSection
import com.glowstudio.android.blindsjn.feature.home.NewsDetailScreen
import com.glowstudio.android.blindsjn.data.model.Article

/**
 * 메인 스크린: 상단바, 하단 네비게이션 바, 내부 컨텐츠(AppNavHost)를 포함하여 전체 화면 전환을 관리합니다.
 */

/**
 * 앱의 메인 UI 구조를 구성하고 화면 간 내비게이션을 관리하는 컴포저블 함수입니다.
 *
 * 상단바와 하단 네비게이션 바를 포함한 Scaffold 레이아웃을 제공하며, NavController를 통해 다양한 화면(홈, 게시판, 인기글, 메시지, 프로필, 상세/작성/추가 화면 등)으로 이동할 수 있습니다.
 * 상단바는 TopBarViewModel의 상태에 따라 동적으로 메인 또는 상세 형태로 전환됩니다.
 * 각 화면 전환 시 필요한 파라미터(예: 게시글 제목, 태그, 게시글 ID, 기사 정보 등)는 URL 디코딩 및 JSON 파싱을 통해 안전하게 처리됩니다.
 */
@Preview
@Composable
fun MainScreen(
    topBarViewModel: TopBarViewModel = viewModel(),
    navigationViewModel: NavigationViewModel = viewModel()
) {
    // 하나의 NavController 생성
    val navController = rememberNavController()
    // TopBarViewModel에서 상단바 상태를 관찰
    val topBarState by topBarViewModel.topBarState.collectAsState()

    Scaffold(
        // 상단바: TopBarViewModel의 상태를 기반으로 동적으로 업데이트됨
        topBar = {
            when (topBarState.type) {
                TopBarType.MAIN -> TopBarMain(
                    rightContent = {
                        // 예시: IconButton 등 원하는 컴포넌트 추가
                    }
                )
                TopBarType.DETAIL -> TopBarDetail(
                    title = topBarState.title,
                    onBackClick = { navController.navigateUp() },
                    onSearchClick = { /* TODO: 검색 동작 구현 */ },
                    onMoreClick = { /* TODO: 더보기 동작 구현 */ }
                )
            }
        },
        // 하단 네비게이션 바
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                viewModel = navigationViewModel
            )
        },
        // 내부 컨텐츠: AppNavHost에 navController와 TopBarViewModel 전달
        content = { paddingValues ->
            // paddingValues에 추가 top padding(예: 16.dp)을 더해 상단바와의 여백을 확보합니다.
            Box(modifier = Modifier.padding(paddingValues)) {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") { HomeScreen(navController) }
                    composable("board") { BoardScreen(navController) }
                    composable("popular") { PopularScreen() }
                    composable("message") { MessageScreen(navController) }
                    composable("profile") { ProfileScreen(
                        onLogoutClick = { /* ... */ },
                        onBusinessCertificationClick = { /* ... */ },
                        onProfileEditClick = { /* ... */ },
                        onContactEditClick = { /* ... */ }
                    ) }
                    composable("boardDetail/{title}") { backStackEntry ->
                        val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
                        val title = URLDecoder.decode(encodedTitle, "UTF-8")
                        BoardDetailScreen(navController, title)
                    }
                    composable(
                        route = "writePost?tags={tags}",
                        arguments = listOf(
                            navArgument("tags") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val tags = backStackEntry.arguments?.getString("tags")
                        WritePostScreen(navController, tags)
                    }
                    composable(
                        route = "postDetail/{postId}",
                        arguments = listOf(
                            navArgument("postId") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getString("postId") ?: ""
                        PostDetailScreen(navController, postId)
                    }
                    composable("addSchedule") {
                        AddScheduleScreen(
                            onCancel = { navController.navigateUp() },
                            onSave = { schedule ->
                                // TODO: 일정 저장 로직 구현
                                navController.navigateUp()
                            }
                        )
                    }
                    composable("news_detail/{articleJson}") { backStackEntry ->
                        val articleJson = backStackEntry.arguments?.getString("articleJson")
                        val article = try {
                            Gson().fromJson(URLDecoder.decode(articleJson, "UTF-8"), Article::class.java)
                        } catch (e: Exception) {
                            null
                        }

                        if (article != null) {
                            NewsDetailScreen(
                                title = article.title ?: "제목 없음",
                                content = article.content,
                                description = article.description,
                                imageUrl = article.urlToImage
                            )
                        }
                    }
                }
            }
        }
    )
}
