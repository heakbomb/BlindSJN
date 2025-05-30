package com.glowstudio.android.blindsjn.feature.ocr.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.ui.theme.*
import com.glowstudio.android.blindsjn.feature.ocr.viewmodel.OcrViewModel
import com.glowstudio.android.blindsjn.feature.ocr.camera.CameraPreview
import com.glowstudio.android.blindsjn.feature.ocr.camera.OcrCameraManager
import com.glowstudio.android.blindsjn.data.network.PermissionManager

@Composable
fun OcrScreen(
    viewModel: OcrViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var cameraManager by remember { mutableStateOf<OcrCameraManager?>(null) }
    val hasCameraPermission by PermissionManager.hasCameraPermission.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        if (hasCameraPermission) {
            // Camera Preview as background
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onCameraReady = { manager ->
                    cameraManager = manager
                }
            )

            // Frame overlay
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp)
                    .size(width = 320.dp, height = 520.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val strokeWidth = 4.dp.toPx()
                    val length = 40.dp.toPx()
                    val w = size.width
                    val h = size.height
                    val color = Blue
                    drawLine(color, Offset(0f, 0f), Offset(length, 0f), strokeWidth)
                    drawLine(color, Offset(0f, 0f), Offset(0f, length), strokeWidth)
                    drawLine(color, Offset(w, 0f), Offset(w - length, 0f), strokeWidth)
                    drawLine(color, Offset(w, 0f), Offset(w, length), strokeWidth)
                    drawLine(color, Offset(0f, h), Offset(0f, h - length), strokeWidth)
                    drawLine(color, Offset(0f, h), Offset(length, h), strokeWidth)
                    drawLine(color, Offset(w, h), Offset(w - length, h), strokeWidth)
                    drawLine(color, Offset(w, h), Offset(w, h - length), strokeWidth)
                }
                Text(
                    "문서를 이 영역에 맞춰주세요.",
                    color = TextSecondary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Capture button
            Button(
                onClick = {
                    cameraManager?.captureImage { imageBytes ->
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        viewModel.processImage(bitmap)
                    }
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
                    .size(72.dp)
                    .clip(CircleShape)
            ) {}
        } else {
            // Permission not granted
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "카메라 권한이 필요합니다",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "앱 설정에서 카메라 권한을 허용해주세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Loading indicator
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Error message
        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OcrScreenPreview() {
    BlindSJNTheme {
        OcrScreen()
    }
}

