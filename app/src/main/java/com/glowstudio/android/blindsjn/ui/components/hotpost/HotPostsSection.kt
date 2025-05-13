package com.glowstudio.android.blindsjn.ui.components.hotpost

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.glowstudio.android.blindsjn.ui.components.common.SectionLayout
import com.glowstudio.android.blindsjn.ui.theme.CardWhite
import com.glowstudio.android.blindsjn.ui.theme.ChatBlue
import com.glowstudio.android.blindsjn.ui.theme.DividerGray
import com.glowstudio.android.blindsjn.ui.theme.Error

data class HotPost(
    val title: String,
    val date: String,
    val likeCount: Int,
    val commentCount: Int
)

/**
 * "인기글" 섹션을 표시하는 컴포저블 함수입니다.
 *
 * 네비게이션 컨트롤러를 받아, "더보기" 버튼 클릭 시 "popular" 경로로 이동합니다.
 * 하드코딩된 인기글 목록을 카드 형태로 보여주며, 각 게시글은 구분선으로 나뉩니다.
 */
@Composable
fun HotPostsSection(navController: NavHostController) {
    // 예시 데이터
    val hotPosts = listOf(
        HotPost("인기글 1", "05/07", 11, 4),
        HotPost("인기글 2", "04/29", 10, 0),
        HotPost("인기글 3", "04/29", 13, 0),
        HotPost("인기글 4", "04/28", 25, 8)
    )

    SectionLayout(
        title = "인기글",
        onMoreClick = { navController.navigate("popular") }
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 0.dp,
            color = CardWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                hotPosts.forEachIndexed { idx, post ->
                    HotPostListItem(post)
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

/**
 * 단일 인기글 항목을 제목, 날짜, 좋아요 및 댓글 수와 함께 가로로 표시합니다.
 *
 * @param post 표시할 인기글 데이터
 */
@Composable
fun HotPostListItem(post: HotPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(post.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.ThumbUp, contentDescription = "좋아요", tint = Error, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${post.likeCount}", color = Error, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(12.dp))
            Icon(Icons.Filled.ChatBubbleOutline, contentDescription = "댓글", tint = ChatBlue, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${post.commentCount}", color = ChatBlue, style = MaterialTheme.typography.bodySmall)
        }
    }
} 