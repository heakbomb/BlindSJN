package com.glowstudio.android.blindsjn.feature.ocr.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResult
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    BlindSJNTheme {
        CameraScreen(
            onNavigateToSalesManagement = { _, _ -> }
        )
    }
} 