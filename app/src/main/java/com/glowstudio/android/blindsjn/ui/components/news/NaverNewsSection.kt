package com.glowstudio.android.blindsjn.ui.components.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.NewsViewModel
import com.glowstudio.android.blindsjn.ui.components.common.SectionLayout
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import com.glowstudio.android.blindsjn.ui.theme.CardWhite
import com.google.gson.Gson
import java.net.URLEncoder
import androidx.core.text.HtmlCompat

/**
 * 네이버 뉴스에서 가져온 최신 소식 목록을 가로 스크롤 형태로 표시하는 컴포저블입니다.
 *
 * 초기 렌더링 시 "자영업" 키워드로 뉴스를 검색하고, 로딩, 오류, 성공 상태에 따라 적절한 UI를 보여줍니다.
 * 각 뉴스 카드는 클릭 시 상세 화면으로 이동합니다.
 *
 * @param navController 뉴스 상세 화면으로의 네비게이션에 사용되는 NavHostController입니다.
 */
@Composable
fun NaverNewsSection(navController: NavHostController) {
    val viewModel: NewsViewModel = viewModel()
    val uiState by viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.searchNews("자영업")
    }

    SectionLayout(title = "새로운 소식") {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Text(uiState.error ?: "오류", color = Color.Red)
            }
            else -> {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 0.dp,
                    color = CardWhite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        items(uiState.newsList) { article ->
                            Box(
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable {
                                        val articleJson = URLEncoder.encode(Gson().toJson(article), "UTF-8")
                                        navController.navigate("news_detail/$articleJson")
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = HtmlCompat.fromHtml(article.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = HtmlCompat.fromHtml(article.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * `NaverNewsSection` 컴포저블의 디자인 미리보기를 제공합니다.
 *
 * 앱 테마와 함께 네비게이션 컨트롤러를 사용하여 뉴스 섹션 UI를 디자인 타임에 시각화할 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun NaverNewsSectionPreview() {
    BlindSJNTheme {
        val navController = rememberNavController()
        NaverNewsSection(navController = navController)
    }
} 