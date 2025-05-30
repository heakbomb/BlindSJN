package com.glowstudio.android.blindsjn.feature.ocr.camera

import android.content.Context
import android.hardware.camera2.CameraManager
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onCameraReady: (OcrCameraManager) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraManager = remember { OcrCameraManager(context) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // Get the back camera ID
                    val systemCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    val cameraId = systemCameraManager.cameraIdList.firstOrNull { id ->
                        val characteristics = systemCameraManager.getCameraCharacteristics(id)
                        val facing = characteristics.get(android.hardware.camera2.CameraCharacteristics.LENS_FACING)
                        facing == android.hardware.camera2.CameraCharacteristics.LENS_FACING_BACK
                    }
                    cameraId?.let { cameraManager.openCamera(it) }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    cameraManager.closeCamera()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            cameraManager.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            SurfaceView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = modifier
    )
} 