package com.glowstudio.android.blindsjn.feature.home

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
import com.google.gson.Gson
import java.net.URLEncoder
import androidx.navigation.compose.rememberNavController
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import androidx.core.text.HtmlCompat
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ChatBubbleOutline
import com.glowstudio.android.blindsjn.ui.theme.*

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
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

        // 인기글 섹션
        HotPostsSection(navController)

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
fun HotPostsSection(navController: NavHostController) {
    // 예시 데이터
    val hotPosts = listOf(
        HotPost("인기글 1", "05/07", 11, 4),
        HotPost("인기글 2", "04/29", 10, 0),
        HotPost("인기글 3", "04/29", 13, 0),
        HotPost("인기글 4", "04/28", 25, 8)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("인기글", fontWeight = FontWeight.Bold)
            TextButton(onClick = { navController.navigate("popular") }) {
                Text("더 보기")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 라운드 사각형 카드로 전체 감싸기
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 0.dp,
            color = CardWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                hotPosts.forEachIndexed { idx, post ->
                    HotPostListItem(post)
                    // 마지막 아이템이 아니면 Divider 추가
                    if (idx != hotPosts.lastIndex) {
                        Divider(
                            color = DividerGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

data class HotPost(
    val title: String,
    val date: String,
    val likeCount: Int,
    val commentCount: Int
)

@Composable
fun HotPostListItem(post: HotPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(post.title, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.ThumbUp, contentDescription = "좋아요", tint = Error, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${post.likeCount}", color = Color.Red, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(12.dp))
            Icon(Icons.Filled.ChatBubbleOutline, contentDescription = "댓글", tint = ChatBlue, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${post.commentCount}", color = ChatBlue, style = MaterialTheme.typography.bodySmall)
        }
    }
}

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
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
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
           // 여기에 뭐 넣을지 고민 좀 해봐야 함

        }
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