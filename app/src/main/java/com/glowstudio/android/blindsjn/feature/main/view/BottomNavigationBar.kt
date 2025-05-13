package com.glowstudio.android.blindsjn.feature.main.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.glowstudio.android.blindsjn.feature.main.viewmodel.NavigationViewModel
import com.glowstudio.android.blindsjn.ui.theme.Blue
import com.glowstudio.android.blindsjn.ui.theme.TextSecondary
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.padding

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "홈화면", Icons.Default.Home)
    object Posts : Screen("board", "게시판 목록", Icons.Default.GridView)
    object Popular : Screen("popular", "인기글", Icons.Default.ThumbUp)
    object Messages : Screen("message", "캘린더", Icons.Default.CalendarToday)
    object Profile : Screen("profile", "프로필", Icons.Default.Person)
    // 아이콘은 material design에서 제공하는 기본 이미지를 사용하여 제작함
    // 추후 변경 예정. 좀 못생겨도 참아주시길
}

/**
 * 하단 네비게이션 바를 표시하여 사용자가 주요 화면 간에 이동할 수 있도록 합니다.
 *
 * 현재 네비게이션 상태를 ViewModel에서 가져와 각 화면에 맞는 아이콘과 라벨을 표시하며,
 * 선택된 항목은 강조 표시됩니다. 항목을 선택하면 해당 화면으로 이동하고 네비게이션 상태가 갱신됩니다.
 *
 * @param navController 네비게이션 동작을 관리하는 컨트롤러
 * @param viewModel 네비게이션 상태를 제공하는 ViewModel
 */
@Composable
fun BottomNavigationBar(
    navController: NavController,
    viewModel: NavigationViewModel
) {
    val navigationState by viewModel.navigationState.collectAsState()

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        navigationState.items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = navController.currentBackStackEntry?.destination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    viewModel.navigateToScreen(screen.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Blue,
                    selectedTextColor = Blue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Blue.copy(alpha = 0f)
                )
            )
        }
    }
}

/**
 * `BottomNavigationBar` 컴포넌트의 미리보기를 앱 테마 내에서 렌더링합니다.
 *
 * Compose 프리뷰에서 네비게이션 바의 외관과 동작을 시각적으로 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BlindSJNTheme {
        BottomNavigationBar(navController = rememberNavController(), viewModel = NavigationViewModel())
    }
}
