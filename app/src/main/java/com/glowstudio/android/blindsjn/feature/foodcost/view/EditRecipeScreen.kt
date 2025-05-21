package com.glowstudio.android.blindsjn.feature.foodcost.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glowstudio.android.blindsjn.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.HorizontalDivider

@Composable
fun EditRecipeScreen(
    recipeName: String = "빵",
    onEditIngredientClick: (String) -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val ingredients = remember {
        listOf("밀가루" to 1000, "물" to 600)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(16.dp)
    ) {
        Text(recipeName, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = TextPrimary)
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("재료 이름", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
            Text("단위(g)", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
            Spacer(Modifier.width(48.dp))
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
        Spacer(Modifier.height(8.dp))
        ingredients.forEach { (name, gram) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(CardWhite, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(name, Modifier.weight(1f), fontSize = 16.sp, color = TextPrimary)
                Text("$gram", Modifier.weight(1f), fontSize = 16.sp, color = TextPrimary)
                TextButton(onClick = { onEditIngredientClick(name) }) {
                    Text("수정", color = Blue, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(4.dp))
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(containerColor = Blue, contentColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("수정", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
} 