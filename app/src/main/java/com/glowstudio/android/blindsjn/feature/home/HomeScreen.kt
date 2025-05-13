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
 * 여러 UI 섹션을 세로로 스크롤 가능한 컬럼에 배치하여 홈 화면을 구성하는 컴포저블입니다.
 *
 * 배너, 네이버 뉴스, 인기글, 오늘의 매출 관리 등 다양한 섹션을 순차적으로 표시하며,
 * 네비게이션 컨트롤러를 통해 각 섹션에서 화면 이동이 가능합니다.
 *
 * @param navController 화면 간 네비게이션을 담당하는 NavHostController 인스턴스
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
 * `HomeScreen`의 디자인 미리보기를 제공하는 Compose 프리뷰 함수입니다.
 *
 * 앱의 테마와 함께 `HomeScreen`을 렌더링하여 디자인 타임에서 UI를 시각적으로 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    BlindSJNTheme {
        HomeScreen(navController = navController)
    }
}