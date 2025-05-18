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
        // 표 헤더
        Row(
            Modifier
                .fillMaxWidth()
                .background(CardWhite, RoundedCornerShape(4.dp))
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text("이름", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("판매가", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("원가", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("마진", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("마진율", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }
        // 표 리스트
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(2.dp, Blue, RoundedCornerShape(4.dp))
                .background(Color.White)
        ) {
            Column(Modifier.fillMaxSize()) {
                // 예시 데이터
                val items = listOf(
                    MarginItem("빵", 5000, 3600, 1400, 24),
                    MarginItem("떡볶이", 6000, 4000, 2000, 33),
                    MarginItem("김밥", 4500, 3000, 1500, 33)
                )
                items.forEach { item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(CardWhite, RoundedCornerShape(2.dp))
                            .padding(vertical = 6.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(item.name, modifier = Modifier.weight(1f))
                        Text("${item.price}원", modifier = Modifier.weight(1f))
                        Text("${item.cost}원", modifier = Modifier.weight(1f))
                        Text("${item.margin}원", modifier = Modifier.weight(1f))
                        Text("${item.marginRate}%", modifier = Modifier.weight(1f))
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
                Spacer(Modifier.height(12.dp))
                MainButton("재료 리스트", onIngredientListClick)
            }
            Column(modifier = Modifier.weight(1f)) {
                MainButton("레시피 등록", onRegisterRecipeClick)
                Spacer(Modifier.height(12.dp))
                MainButton("재료 등록", onRegisterIngredientClick)
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
fun ChartTabButton(text: String, selected: Boolean) {
    Box(
        Modifier
            .padding(end = 8.dp)
            .background(if (selected) Blue else Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Blue, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(text, color = if (selected) Color.White else Blue, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BarChartBar(label: String, percent: Float, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .width(32.dp)
                .height((100 * percent).dp)
                .background(Blue, RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.height(4.dp))
        Text(label, color = TextSecondary, fontSize = 14.sp)
        Text("$value", color = TextSecondary, fontSize = 14.sp)
    }
}

@Composable
fun PieChart(percent1: Float, percent2: Float) {
    // 실제 파이차트 대신 원형 Box 2개로 대체 (예시)
    Box(Modifier.size(80.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(80.dp)
                .background(LightBlue, shape = RoundedCornerShape(40.dp))
        )
        Box(
            Modifier
                .size(80.dp * percent1)
                .background(Blue, shape = RoundedCornerShape(40.dp))
        )
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

data class MarginItem(
    val name: String,
    val price: Int,
    val cost: Int,
    val margin: Int,
    val marginRate: Int
)

@Preview(showBackground = true)
@Composable
fun MarginListScreenPreview() {
    MarginListScreen()
} 