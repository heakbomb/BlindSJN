package com.glowstudio.android.blindsjn.feature.main.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationState(
    val currentRoute: String = "home_screen",
    val items: List<Screen> = listOf(
        Screen.Home,
        Screen.Posts,
        Screen.Popular,
        Screen.Messages,
        Screen.Profile
    )
)

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home_screen", "홈화면", Icons.Default.Home)
    object Posts : Screen("board_root", "게시판 목록", Icons.Default.GridView)
    object Popular : Screen("popular_root", "인기글", Icons.Default.ThumbUp)
    object Messages : Screen("message_root", "캘린더", Icons.Default.CalendarToday)
    object Profile : Screen("profile_root", "프로필", Icons.Default.Person)
} 