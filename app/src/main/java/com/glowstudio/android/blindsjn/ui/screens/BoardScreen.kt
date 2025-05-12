package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

/**
 * 게시판 진입점 화면.
 * 실제로는 게시판 카테고리 선택 UI인 BoardCategoryScreen을 보여줍니다.
 */
@Composable
fun BoardScreen(
    navController: NavController
) {
    BoardCategoryScreen(navController = navController)
}
