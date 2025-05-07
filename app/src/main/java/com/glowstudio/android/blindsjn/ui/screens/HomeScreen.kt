package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.R
import com.glowstudio.android.blindsjn.ui.NewsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.gson.Gson
import java.net.URLEncoder
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import androidx.core.text.HtmlCompat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        // 배너 섹션
        BannerSection()

        // 바로가기 섹션
        ShortcutSection(navController)

        // 네이버 뉴스 섹션
        NaverNewsSection(navController)

        // 오늘의 매출 관리 섹션
        SalesSection()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerSection() {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFF5F7FF))
    ) {
        HorizontalPager(
            count = 4,
            state = pagerState,
        ) { page ->
            Image(
                painter = painterResource(id = R.drawable.login_image),
                contentDescription = "배너 이미지 $page",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // 페이지 인디케이터
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = Color.Gray.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun ShortcutSection(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("바로가기", fontWeight = FontWeight.Bold)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "더보기")
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(shortcutItems) { item ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .clickable {
                            when (item.title) {
                                "푸드코스트" -> navController.navigate("foodCost")
                                "캘린더" -> navController.navigate("message")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.emoji,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// 바로가기 아이템 데이터 클래스
data class ShortcutItem(
    val title: String,
    val emoji: String
)

// 바로가기 아이템 목록
private val shortcutItems = listOf(
    ShortcutItem("푸드코스트", "🍴"),
    ShortcutItem("캘린더", "📅")
)

@Composable
fun NaverNewsSection(navController: NavHostController) {
    val viewModel: NewsViewModel = viewModel()
    val uiState by viewModel.uiState

    // 화면 진입 시 기본 검색어로 뉴스 검색
    LaunchedEffect(Unit) {
        viewModel.searchNews("자영업")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("새로운 소식", fontWeight = FontWeight.Bold)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "더보기")
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Text(uiState.error ?: "오류", color = Color.Red)
            }
            else -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.newsList) { article ->
                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .clickable {
                                    val articleJson = URLEncoder.encode(Gson().toJson(article), "UTF-8")
                                    navController.navigate("newsDetail/$articleJson")
                                }
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
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
//                                    Text(
//                                        text = HtmlCompat.fromHtml(article.link, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
//                                        maxLines = 3,
//                                        overflow = TextOverflow.Ellipsis
//                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SalesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("오늘의 매출 관리", fontWeight = FontWeight.Bold)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "더보기")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularSalesChart(percentage = 0.65f, label = "65%")
        }
    }
}

@Composable
//샘플용 원형 그래프
fun CircularSalesChart(percentage: Float, label: String) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val sweepAngle = percentage * 360f

            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.Blue,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
        }

        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    BlindSJNTheme {
        HomeScreen(navController = navController)
    }
}