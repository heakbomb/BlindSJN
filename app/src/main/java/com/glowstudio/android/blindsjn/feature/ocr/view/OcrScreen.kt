package com.glowstudio.android.blindsjn.feature.ocr.view

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OcrScreen(
    viewModel: OcrViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    // Create a temporary file URI for the camera
    val imageUri = remember {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "OCR_$timestamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/BlindSJN")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }
        
        context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            viewModel.processImage(imageUri, context)
        }
    }

    val verticalPadding = 48.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // 상단에 프레임
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = verticalPadding)
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

        // 하단에 버튼
        Button(
            onClick = {
                imageUri?.let { uri ->
                    cameraLauncher.launch(uri)
                }
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = verticalPadding)
                .size(72.dp)
                .clip(CircleShape)
        ) {}

        // Show loading indicator when processing
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Show error message if any
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

