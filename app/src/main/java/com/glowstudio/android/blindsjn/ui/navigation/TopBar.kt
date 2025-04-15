package com.glowstudio.android.blindsjn.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String, // 상단 바의 제목
    showBackButton: Boolean = true, // 뒤로가기 버튼 표시 여부
    showSearchButton: Boolean = true, // 검색 버튼 표시 여부
    onBackClick: () -> Unit = {}, // 뒤로가기 버튼 클릭 이벤트
    onSearchClick: () -> Unit = {} // 검색 버튼 클릭 이벤트
) {
    CenterAlignedTopAppBar(
        title = { Text(title) }, // 제목 표시
        navigationIcon = {
            if (showBackButton) { // 뒤로가기 버튼 표시 여부
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                }
            }
        },
        actions = {
            if (showSearchButton) { // 검색 버튼 표시 여부
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Filled.Search, contentDescription = "검색")
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp)
    )
}
