package com.glowstudio.android.blindsjn.feature.ocr.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrItem
import com.glowstudio.android.blindsjn.ui.theme.*

@Composable
fun OcrResultScreen(
    items: List<OcrItem>,
    onItemEdit: (Int, OcrItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "영수증 분석 결과",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.size) { index ->
                OcrItemCard(
                    item = items[index],
                    onEdit = { onItemEdit(index, it) }
                )
            }
        }
    }
}

@Composable
fun OcrItemCard(
    item: OcrItem,
    onEdit: (OcrItem) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "수량: ${item.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "가격: ${item.price}원",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "수",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    
    if (showEditDialog) {
        EditOcrItemDialog(
            item = item,
            onDismiss = { showEditDialog = false },
            onConfirm = { 
                onEdit(it)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditOcrItemDialog(
    item: OcrItem,
    onDismiss: () -> Unit,
    onConfirm: (OcrItem) -> Unit
) {
    var name by remember { mutableStateOf(item.name) }
    var quantity by remember { mutableStateOf(item.quantity.toString()) }
    var price by remember { mutableStateOf(item.price.toString()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("상품 정보 수정") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("상품명") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("수량") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("가격") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        OcrItem(
                            name = name,
                            quantity = quantity.toIntOrNull() ?: item.quantity,
                            price = price.toIntOrNull() ?: item.price
                        )
                    )
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OcrResultScreenPreview() {
    BlindSJNTheme {
        OcrResultScreen(
            items = listOf(
                OcrItem(
                    name = "아메리카노",
                    quantity = 2,
                    price = 4000
                ),
                OcrItem(
                    name = "카페라떼",
                    quantity = 1,
                    price = 4500
                ),
                OcrItem(
                    name = "크로와상",
                    quantity = 3,
                    price = 3500
                )
            ),
            onItemEdit = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OcrItemCardPreview() {
    BlindSJNTheme {
        OcrItemCard(
            item = OcrItem(
                name = "아메리카노",
                quantity = 2,
                price = 4000
            ),
            onEdit = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditOcrItemDialogPreview() {
    BlindSJNTheme {
        EditOcrItemDialog(
            item = OcrItem(
                name = "아메리카노",
                quantity = 2,
                price = 4000
            ),
            onDismiss = { },
            onConfirm = { }
        )
    }
} 