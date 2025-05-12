package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.shadow               // ← 수정된 임포트
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController  // ← Preview 용
import com.glowstudio.android.blindsjn.ui.theme.Blue
import com.glowstudio.android.blindsjn.ui.theme.TextSecondary

/**
 * 하단 네비게이션에 사용되는 화면 정보
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home       : Screen("home",           "홈화면",     Icons.Default.Home)
    object Posts      : Screen("boardCategory",  "게시판",     Icons.Default.GridView)
    object Management : Screen("management",     "매출관리",   Icons.Default.ShoppingCart)
    object Messages   : Screen("message",        "캘린더",     Icons.Default.CalendarToday)
    object Profile    : Screen("profile",        "프로필",     Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Posts,
        Screen.Management,
        Screen.Messages,
        Screen.Profile
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .padding(top = 8.dp)
            .shadow(elevation = 8.dp)     // ← shadow modifier
            .fillMaxWidth()
            .height(65.dp)
    ) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor    = Blue,
                    selectedTextColor    = Blue,
                    unselectedIconColor  = TextSecondary,
                    unselectedTextColor  = TextSecondary,
                    indicatorColor       = Blue.copy(alpha = 0.1f)
                ),
                alwaysShowLabel = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(navController = rememberNavController())
}
