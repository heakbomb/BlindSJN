package com.glowstudio.android.blindsjn.feature.main.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.glowstudio.android.blindsjn.feature.main.viewmodel.NavigationViewModel
import com.glowstudio.android.blindsjn.ui.theme.Blue
import com.glowstudio.android.blindsjn.ui.theme.TextSecondary
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import com.glowstudio.android.blindsjn.feature.main.model.Screen

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
