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

/**
 * 게시판 카테고리 목록을 2열 그리드로 표시하는 Compose 화면입니다.
 *
 * 각 카테고리 항목을 클릭하면 해당 카테고리의 상세 화면으로 이동합니다.
 */
@Composable
fun BoardScreen(navController: NavController) {
    val viewModel: BoardViewModel = viewModel()
    val boardCategories by viewModel.boardCategories.collectAsState()

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
                            navController.navigate("boardDetail/${category.title}")
                        }
                    )
                }
            }
        }
    )
}

/**
 * 보드 카테고리를 이모지와 제목으로 표시하는 클릭 가능한 행 컴포저블입니다.
 *
 * @param category 표시할 보드 카테고리 데이터입니다.
 * @param onClick 카테고리 선택 시 호출되는 콜백입니다.
 */
@Composable
fun BoardCategoryItem(category: BoardCategory, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(4.dp)
            .border(1.dp, DividerGray, MaterialTheme.shapes.medium)
            .background(CardWhite, MaterialTheme.shapes.medium)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Blue.copy(alpha = 0.1f), MaterialTheme.shapes.medium),
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
                color = TextPrimary
            )
        }
    }
}

/**
 * `BoardScreen`의 미리보기를 제공하는 Compose 프리뷰 함수입니다.
 *
 * 앱 테마와 네비게이션 컨트롤러를 적용하여 실제 환경과 유사하게 `BoardScreen` UI를 미리 확인할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun BoardScreenPreview() {
    BlindSJNTheme {
        val navController = rememberNavController()
        BoardScreen(navController = navController)
    }
}

/**
 * `BoardCategoryItem` 컴포저블의 미리보기를 표시합니다.
 *
 * 샘플 게시판 카테고리("자유게시판", 이모지 "💬")를 사용하여 앱 테마 내에서 항목의 스타일과 레이아웃을 확인할 수 있습니다.
 */
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
