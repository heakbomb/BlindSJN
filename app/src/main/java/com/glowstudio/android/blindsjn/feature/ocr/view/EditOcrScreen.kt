package com.glowstudio.android.blindsjn.feature.ocr.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResult
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme

@Composable
fun EditOcrScreen(
    results: List<OcrResult>,
    onUpdateResult: (Int, OcrResult) -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "결과 수정",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 결과 테이블
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 헤더
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "이름",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "수량",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "가격",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline
                )

                // 결과 항목들
                results.forEachIndexed { index, result ->
                    EditOcrItem(
                        result = result,
                        onUpdate = { updatedResult ->
                            onUpdateResult(index, updatedResult)
                        }
                    )
                    if (index < results.size - 1) {
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // 확인 버튼
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("확인")
        }
    }
}

@Composable
private fun EditOcrItem(
    result: OcrResult,
    onUpdate: (OcrResult) -> Unit
) {
    var name by remember { mutableStateOf(result.name) }
    var quantity by remember { mutableStateOf(result.quantity.toString()) }
    var price by remember { mutableStateOf(result.price.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이름 입력
        OutlinedTextField(
            value = name,
            onValueChange = { 
                name = it
                onUpdate(OcrResult(name, quantity.toIntOrNull() ?: 1, price.toIntOrNull() ?: 0))
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 수량 입력
        OutlinedTextField(
            value = quantity,
            onValueChange = { 
                if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                    quantity = it
                    onUpdate(OcrResult(name, quantity.toIntOrNull() ?: 1, price.toIntOrNull() ?: 0))
                }
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 가격 입력
        OutlinedTextField(
            value = price,
            onValueChange = { 
                if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                    price = it
                    onUpdate(OcrResult(name, quantity.toIntOrNull() ?: 1, price.toIntOrNull() ?: 0))
                }
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditOcrScreenPreview() {
    BlindSJNTheme {
        EditOcrScreen(
            results = listOf(
                OcrResult("김치찌개", 1, 8000),
                OcrResult("된장찌개", 2, 7000),
                OcrResult("비빔밥", 1, 9000)
            ),
            onUpdateResult = { _, _ -> },
            onConfirm = {}
        )
    }
} 