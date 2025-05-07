package com.glowstudio.android.blindsjn.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.navigation.AppNavHost
import com.glowstudio.android.blindsjn.ui.navigation.BottomNavigationBar
import com.glowstudio.android.blindsjn.ui.navigation.TopBar
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.tooling.preview.Preview

/**
 * 메인 스크린: 상단바, 하단 네비게이션 바, 내부 컨텐츠(AppNavHost)를 포함하여 전체 화면 전환을 관리합니다.
 */

@Preview
@Composable
fun MainScreen(topBarViewModel: TopBarViewModel = viewModel()) {
    // 하나의 NavController 생성
    val navController = rememberNavController()
    // TopBarViewModel에서 상단바 상태를 관찰
    val topBarState by topBarViewModel.topBarState.collectAsState()

    Scaffold(
        // 상단바: TopBarViewModel의 상태를 기반으로 동적으로 업데이트됨
        topBar = {
            TopBar(
                title = topBarState.title,
                showBackButton = topBarState.showBackButton,
                showSearchButton = topBarState.showSearchButton,
                onBackClick = { navController.navigateUp() },
                onSearchClick = { /* TODO: 검색 동작 구현 */ }
            )
        },
        // 하단 네비게이션 바
        bottomBar = {
            BottomNavigationBar(navController)
        },
        // 내부 컨텐츠: AppNavHost에 navController와 TopBarViewModel 전달
        content = { paddingValues ->
            // paddingValues에 추가 top padding(예: 16.dp)을 더해 상단바와의 여백을 확보합니다.
            Box(modifier = Modifier.padding(paddingValues)) {
                AppNavHost(
                    navController = navController,
                    topBarViewModel = topBarViewModel
                )
            }
        }
    )
}
