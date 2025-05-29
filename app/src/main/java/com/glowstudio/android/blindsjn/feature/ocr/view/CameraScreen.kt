package com.glowstudio.android.blindsjn.feature.ocr.view

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResult
import com.glowstudio.android.blindsjn.feature.ocr.viewmodel.CameraViewModel
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import com.glowstudio.android.blindsjn.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import com.glowstudio.android.blindsjn.ui.theme.ColorPalette

@Composable
fun CameraScreen(
    onNavigateToOcrResult: (List<OcrResult>) -> Unit,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val ocrResults by viewModel.ocrResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // OCR 결과가 있을 때 자동으로 결과 화면으로 이동
    LaunchedEffect(ocrResults) {
        if (ocrResults.isNotEmpty()) {
            onNavigateToOcrResult(ocrResults)
        }
    }

    // 에러가 있을 때 처리
    LaunchedEffect(error) {
        error?.let {
            android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_LONG).show()
        }
    }

    // 카메라 관련 상태
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraExecutor = remember { ContextCompat.getMainExecutor(context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.White)
    ) {
        // 카메라 프리뷰
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = CameraXPreview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, cameraExecutor)
        }

        // 프레임 오버레이
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

        // 로딩 인디케이터
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPalette.DarkBackground.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = ColorPalette.Blue
                )
            }
        }

        // 촬영 버튼
        Button(
            onClick = {
                takePhoto(
                    imageCapture = imageCapture,
                    outputDirectory = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)!!,
                    executor = cameraExecutor,
                    onImageCaptured = { uri ->
                        viewModel.processImage(uri)
                    },
                    onError = { /* 에러 처리 */ }
                )
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = ColorPalette.Blue),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .size(72.dp)
                .clip(CircleShape)
        ) {}
    }
}

private fun takePhoto(
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = File(
        outputDirectory,
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA)
            .format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                onImageCaptured(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        }
    )
}

@Composable
fun OcrResultScreen(
    results: List<OcrResult>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "이 결과가 맞나요?",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
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

                results.forEach { result ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = result.name,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = result.quantity.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = result.price.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
} 