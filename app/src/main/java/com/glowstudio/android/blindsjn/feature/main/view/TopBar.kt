package com.glowstudio.android.blindsjn.feature.main.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.RowScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.glowstudio.android.blindsjn.R

/**
 * 중앙에 정렬된 상단 앱 바를 표시하며, 왼쪽에 클릭 가능한 로고 아이콘과 오른쪽에 커스텀 액션 콘텐츠를 배치합니다.
 *
 * @param modifier 앱 바의 레이아웃 및 스타일을 지정할 때 사용합니다.
 * @param onLogoClick 로고 아이콘 클릭 시 호출되는 람다입니다.
 * @param rightContent 오른쪽에 표시할 추가 액션 아이콘 등의 컴포저블 콘텐츠를 정의합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMain(
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit = {},
    rightContent: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onLogoClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "로고",
                    tint = Color.Unspecified
                )
            }
        },
        actions = rightContent,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * 가운데 정렬된 상단 앱 바를 표시하며, 제목과 뒤로가기, 검색, 더보기 아이콘을 제공합니다.
 *
 * @param title 상단 바에 표시할 텍스트 제목입니다.
 * @param onBackClick 뒤로가기 아이콘 클릭 시 호출되는 람다입니다.
 * @param onSearchClick 검색 아이콘 클릭 시 호출되는 람다입니다.
 * @param onMoreClick 더보기 아이콘 클릭 시 호출되는 람다입니다.
 * @param modifier 상단 바의 레이아웃 및 스타일을 지정하는 Modifier입니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarDetail(
    title: String,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Filled.Search, contentDescription = "검색")
            }
            IconButton(onClick = onMoreClick) {
                Icon(Icons.Filled.MoreVert, contentDescription = "더보기")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * `TopBarMain`의 미리보기를 렌더링하는 컴포저블입니다.
 *
 * 세 개의 액션 아이콘(검색, 더보기, 임시 뒤로가기 아이콘)이 포함된 상단 앱 바의 예시를 보여줍니다.
 */
@Preview(showBackground = true)
@Composable
fun TopBarMainPreview() {
    TopBarMain(
        rightContent = {
            IconButton(onClick = {}) { Icon(Icons.Filled.Search, contentDescription = "검색") }
            IconButton(onClick = {}) { Icon(Icons.Filled.MoreVert, contentDescription = "더보기") }
            IconButton(onClick = {}) { Icon(Icons.Filled.ArrowBack, contentDescription = "임시") }
        }
    )
}

/**
 * `TopBarDetail`의 미리보기를 표시하는 컴포저블입니다.
 *
 * 미리보기에서는 "상세화면"이라는 제목과 기본 클릭 핸들러를 사용합니다.
 */
@Preview(showBackground = true)
@Composable
fun TopBarDetailPreview() {
    TopBarDetail(
        title = "상세화면",
        onBackClick = {},
        onSearchClick = {},
        onMoreClick = {}
    )
}
