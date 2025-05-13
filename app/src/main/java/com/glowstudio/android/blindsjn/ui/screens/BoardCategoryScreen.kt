// File: com/glowstudio/android/blindsjn/ui/screens/BoardCategoryScreen.kt
package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.model.Category
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work

@Composable
fun BoardCategoryScreen(navController: NavController) {
    val categories = listOf(
        Category("직종게시판", Icons.Default.Work,       "jobBoard"),
        Category("자유게시판", Icons.Default.ChatBubble, "freeBoard"),
        Category("질문게시판", Icons.Default.Help,       "qnaBoard"),
        Category("인기게시판", Icons.Default.Star,       "popularBoard")
    )

    LazyColumn(
        modifier            = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clickable { navController.navigate(category.route) },
                shape     = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector        = category.icon,
                        contentDescription = category.title,
                        modifier           = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text  = category.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
