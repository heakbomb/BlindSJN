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
    object Popular : Screen("popular", "매출관리", Icons.Default.Payment)
    object Messages : Screen("message", "캘린더", Icons.Default.CalendarToday)
    object Profile : Screen("profile", "프로필", Icons.Default.Person)
    // 아이콘은 material design에서 제공하는 기본 이미지를 사용하여 제작함
    // 추후 변경 예정. 좀 못생겨도 참아주시길
}

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

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BlindSJNTheme {
        BottomNavigationBar(navController = rememberNavController(), viewModel = NavigationViewModel())
    }
}
