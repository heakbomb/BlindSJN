package com.glowstudio.android.blindsjn.feature.foodcost.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glowstudio.android.blindsjn.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import com.glowstudio.android.blindsjn.feature.foodcost.model.MarginItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.BorderStroke

@Composable
fun MarginListScreen(
    onRecipeListClick: () -> Unit = {},
    onRegisterRecipeClick: () -> Unit = {},
    onIngredientListClick: () -> Unit = {},
    onRegisterIngredientClick: () -> Unit = {},
    onNavigateToPayManagement: () -> Unit = {},
    onNavigateToMargin: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(16.dp)
    ) {
        // 상단 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TabButton(text = "매출관리", selected = false, onClick = onNavigateToPayManagement, modifier = Modifier.weight(1f))
            TabButton(text = "마진관리", selected = true, onClick = onNavigateToMargin, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        // 버블 그래프 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            border = BorderStroke(1.dp, DividerGray)
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(com.glowstudio.android.blindsjn.feature.foodcost.viewmodel.MarginListViewModel().items) { item ->
                        MarginBubble(
                            name = item.name,
                            sales = item.price,
                            cost = item.cost,
                            marginRate = item.marginRate,
                            color = item.color,
                            marginColor = item.marginColor,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
        // 순위표 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            border = BorderStroke(1.dp, DividerGray)
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp, max = 400.dp)
                        .padding(top = 4.dp),
                    userScrollEnabled = true
                ) {
                    itemsIndexed(com.glowstudio.android.blindsjn.feature.foodcost.viewmodel.MarginListViewModel().items) { idx, item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 순위 원형 배지
                            Box(
                                Modifier
                                    .size(28.dp)
                                    .background(item.color, shape = androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${idx + 1}등",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            // 이름
                            Text(
                                item.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1.2f)
                            )
                            // 판매금액
                            Text(
                                "% ,d원".format(item.price),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                            // 순수익(판매가-원가)
                            Text(
                                "% ,d원".format(item.price - item.cost),
                                color = Color(0xFF388E3C),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                            // 마진율
                            Text(
                                "${item.marginRate}%",
                                color = Color(0xFF1976D2),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(0.7f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        // 하단 버튼들
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                MainButton("레시피 리스트", onRecipeListClick)
            }
            Column(modifier = Modifier.weight(1f)) {
                MainButton("레시피 등록", onRegisterRecipeClick)
            }
        }
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Blue else Color.White,
            contentColor = if (selected) Color.White else Blue
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
fun MainButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Blue, contentColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun MarginBubble(
    name: String,
    sales: Int,
    cost: Int,
    marginRate: Int,
    color: Color,
    marginColor: Color,
    modifier: Modifier = Modifier
) {
    // 버블 크기 축소: 기본값 60, 매출에 따라 최대 50까지 증가
    val bubbleSize = (60 + (sales / 30000).coerceAtMost(50)).dp
    val marginRatio = marginRate / 100f
    Box(
        modifier = modifier.size(bubbleSize),
        contentAlignment = Alignment.TopCenter
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val R = size.minDimension / 2f
            val r = R * marginRatio
            drawCircle(
                color = color,
                radius = R,
                center = androidx.compose.ui.geometry.Offset(R, R)
            )
            drawCircle(
                color = marginColor,
                radius = r,
                center = androidx.compose.ui.geometry.Offset(R, R + (R - r))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(1.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                    Text("${sales}원", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarginListScreenPreview() {
    MarginListScreen()
} 