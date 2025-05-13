package com.glowstudio.android.blindsjn.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import com.glowstudio.android.blindsjn.ui.components.banner.BannerSection
import com.glowstudio.android.blindsjn.ui.components.shortcut.ShortcutSection
import com.glowstudio.android.blindsjn.ui.components.news.NaverNewsSection
import com.glowstudio.android.blindsjn.ui.components.hotpost.HotPostsSection
import com.glowstudio.android.blindsjn.ui.components.sales.SalesSection

/**
 * 홈 화면의 주요 섹션들을 세로 스크롤 레이아웃으로 표시하는 컴포저블 함수입니다.
 *
 * 배너, 네이버 뉴스, 인기글, 오늘의 매출 관리 등 다양한 UI 섹션을 순차적으로 렌더링하며,
 * 네비게이션 처리를 위해 NavHostController를 전달받습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        // 배너 섹션
        BannerSection()

        // 바로가기 섹션
        // ShortcutSection(navController)

        // 네이버 뉴스 섹션
        NaverNewsSection(navController)

        // 인기글 섹션
        HotPostsSection(navController)

        // 오늘의 매출 관리 섹션
        SalesSection()
    }
}

/**
 * `HomeScreen`의 디자인 미리보기를 제공합니다.
 *
 * 앱의 테마와 함께 네비게이션 컨트롤러를 초기화하여 IDE에서 홈 화면 UI를 시각적으로 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    BlindSJNTheme {
        HomeScreen(navController = navController)
    }
}