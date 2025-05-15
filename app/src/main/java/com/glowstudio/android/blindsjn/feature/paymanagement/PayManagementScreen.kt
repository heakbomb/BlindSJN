package com.glowstudio.android.blindsjn.feature.paymanagement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import com.glowstudio.android.blindsjn.ui.theme.*
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.Paint

@Composable
fun PayManagementScreen(
    onNavigateToFoodCost: () -> Unit = {},
    onNavigateToSalesInput: () -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(0) } // 0:일, 1:주, 2:월, 3:년
    val tabTitles = listOf("일", "주", "월", "연")

    // 샘플 데이터
    val barData = listOf(65000, 80000, 50000)
    val barLabels = listOf("05-14", "05-15", "05-16")
    val pieData = listOf(0.35f, 0.65f) // 떡볶이, 김밥
    val pieLabels = listOf("떡볶이", "김밥")
    val pieColors = listOf(LightBlue, Color(0xFFB3E5FC)) // 더 연한 파란색 계열

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        // 매출관리/마진관리 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .clickable { /* 현재 화면 */ },
                contentAlignment = Alignment.Center
            ) {
                Text("매출관리", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToFoodCost() },
                contentAlignment = Alignment.Center
            ) {
                Text("마진관리", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = DividerGray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))
        // 오늘의 매출입력
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            CommonButton(
                text = "오늘의 매출입력",
                onClick = onNavigateToSalesInput,
                modifier = Modifier.width(180.dp).height(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 막대그래프 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            border = BorderStroke(1.dp, DividerGray)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // 일/주/월/년 탭
                Row {
                    tabTitles.forEachIndexed { idx, title ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedTab == idx) MaterialTheme.colorScheme.primary else CardWhite)
                                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                .clickable { selectedTab = idx }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(title, color = if (selectedTab == idx) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // 막대그래프
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    barData.forEachIndexed { idx, value ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 날짜
                            Text(barLabels[idx], fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 1.dp,
                                modifier = Modifier.width(36.dp)
                            )
                            // 막대
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height((value / 1000).dp)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                            )
                            // 값 (막대 아래)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${value}", fontSize = 13.sp, color = TextHint)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // 원그래프 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            border = BorderStroke(1.dp, DividerGray)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // 일/주/월/년 탭
                Row {
                    tabTitles.forEachIndexed { idx, title ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedTab == idx) MaterialTheme.colorScheme.primary else CardWhite)
                                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                .clickable { selectedTab = idx }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(title, color = if (selectedTab == idx) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // 원그래프 + 범례
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(200.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PieChart(
                            proportions = pieData,
                            colors = pieColors
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        pieLabels.forEachIndexed { idx, label ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(pieColors[idx], RoundedCornerShape(10.dp))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(label, fontSize = 15.sp, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                // 비율 표시
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Text("65%", fontSize = 14.sp, color = TextSecondary)
                    //Text("35%", fontSize = 14.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun PieChart(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Canvas(
        modifier = modifier.size(140.dp)
    ) {
        var startAngle = -90f
        proportions.forEachIndexed { idx, proportion ->
            val sweep = proportion * 360f
            drawArc(
                color = colors[idx],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true
            )
            // % 표기 (중앙 각도 계산)
            val angle = startAngle + sweep / 2
            val radius = size.minDimension / 2.5f
            val percent = (proportion * 100).toInt()
            val x = center.x + radius * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = center.y + radius * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()

            drawIntoCanvas { canvas ->
                val paint = Paint().asFrameworkPaint().apply {
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 32f
                    color = android.graphics.Color.BLACK
                    isFakeBoldText = true
                }
                canvas.nativeCanvas.drawText(
                    "$percent%",
                    x,
                    y + 10f,
                    paint
                )
            }
            startAngle += sweep
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PayManagementScreenPreview() {
    PayManagementScreen()
} 