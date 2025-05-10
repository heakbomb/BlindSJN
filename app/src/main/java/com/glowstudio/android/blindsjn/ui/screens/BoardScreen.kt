package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class BoardCategory(
    val emoji: String,
    val title: String,
    val postCount: String,
    val category: String // 실제 카테고리 값 추가
)

val boardCategories = listOf(
    BoardCategory("🍴", "음식점 및 카페", "298개의 새 게시물", "FOOD"),
    BoardCategory("🛍️", "쇼핑 및 리테일", "128개의 새 게시물", "SHOPPING"),
    BoardCategory("💊", "건강 및 의료", "57개의 새 게시물", "HEALTH"),
    BoardCategory("🏨", "숙박 및 여행", "298개의 새 게시물", "TRAVEL"),
    BoardCategory("📚", "교육 및 학습", "36개의 새 게시물", "EDUCATION"),
    BoardCategory("🎮", "여가 및 오락", "98개의 새 게시물", "LEISURE"),
    BoardCategory("💰", "금융 및 공공기관", "20개의 새 게시물", "FINANCE")
)

@Composable
fun BoardScreen(navController: NavController) {
    Scaffold(
        content = { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(boardCategories) { category ->
                    BoardCategoryItem(
                        category = category,
                        onClick = {
                            navController.navigate("boardDetail/${category.category}")
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
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.emoji,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = category.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = category.postCount,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}