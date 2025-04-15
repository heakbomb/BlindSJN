package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FoodCostScreen(
    onRegisterRecipeClick: () -> Unit,
    onRegisterIngredientClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onRegisterRecipeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("레시피 등록")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRegisterIngredientClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("재료 등록")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 추후 Room 등에서 불러올 레시피 리스트
        var selectedRecipe by remember { mutableStateOf("") }
        val recipeList = listOf("김치찌개", "된장국") // 예시

        DropdownMenuBox(
            title = "레시피 선택",
            items = recipeList,
            selectedItem = selectedRecipe,
            onItemSelected = { selectedRecipe = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // TODO: 실제 푸드코스트 계산 로직 연결
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("계산")
        }
    }
}

@Composable
fun DropdownMenuBox(
    title: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = { Text(title) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onItemSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
