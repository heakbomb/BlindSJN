package com.glowstudio.android.blindsjn.feature.board.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.feature.board.viewmodel.BoardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.feature.board.model.BoardCategory
import com.glowstudio.android.blindsjn.ui.theme.*
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme

@Composable
fun BoardScreen(navController: NavController) {
    val viewModel: BoardViewModel = viewModel()
    val boardCategories by viewModel.boardCategories.collectAsState()

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                boardCategories.forEach { category ->
                    BoardCategoryItem(
                        category = category,
                        onClick = {
                            navController.navigate("boardDetail/${category.route}")
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun BoardCategoryItem(category: BoardCategory, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(CardWhite, MaterialTheme.shapes.medium)
            .padding(vertical = 18.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Blue.copy(alpha = 0.1f), MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.emoji,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.title,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BoardScreenPreview() {
    BlindSJNTheme {
        val navController = rememberNavController()
        BoardScreen(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun BoardCategoryItemPreview() {
    BlindSJNTheme {
        BoardCategoryItem(
            category = BoardCategory(
                title = "자유게시판",
                emoji = "💬",
                route = "freeBoard"
            ),
            onClick = {}
        )
    }
}
