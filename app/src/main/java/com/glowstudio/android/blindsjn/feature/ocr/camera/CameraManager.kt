package com.glowstudio.android.blindsjn.feature.ocr.camera

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Executors

class OcrCameraManager(private val context: Context) {
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private val cameraThread = HandlerThread("CameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    
    private val _cameraState = MutableStateFlow<CameraState>(CameraState.Closed)
    val cameraState: StateFlow<CameraState> = _cameraState

    private var imageReader: ImageReader? = null
    private var onImageCaptured: ((ByteArray) -> Unit)? = null
    private var previewSurface: Surface? = null

    init {
        imageReader = ImageReader.newInstance(
            1920, 1080, ImageFormat.JPEG, 2
        ).apply {
            setOnImageAvailableListener({ reader ->
                val image = reader.acquireLatestImage()
                try {
                    val buffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    onImageCaptured?.invoke(bytes)
                } finally {
                    image.close()
                }
            }, cameraHandler)
        }
    }

    fun setPreviewSurface(surface: Surface) {
        previewSurface = surface
    }

    fun openCamera(cameraId: String) {
        val systemCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager
        try {
            systemCameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    _cameraState.value = CameraState.Opened
                    createCaptureSession()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                    cameraDevice = null
                    _cameraState.value = CameraState.Closed
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                    cameraDevice = null
                    _cameraState.value = CameraState.Error("Camera error: $error")
                }
            }, cameraHandler)
        } catch (e: SecurityException) {
            _cameraState.value = CameraState.Error("Camera permission not granted")
        } catch (e: CameraAccessException) {
            _cameraState.value = CameraState.Error("Camera access error: ${e.message}")
        }
    }

    private fun createCaptureSession() {
        val surfaces = mutableListOf<Surface>()
        previewSurface?.let { surfaces.add(it) }
        imageReader?.surface?.let { surfaces.add(it) }

        if (surfaces.isEmpty()) {
            _cameraState.value = CameraState.Error("No surfaces available for preview")
            return
        }

        cameraDevice?.createCaptureSession(surfaces, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                captureSession = session
                try {
                    val requestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    surfaces.forEach { surface ->
                        requestBuilder?.addTarget(surface)
                    }
                    session.setRepeatingRequest(requestBuilder?.build()!!, null, cameraHandler)
                } catch (e: CameraAccessException) {
                    _cameraState.value = CameraState.Error("Failed to start camera preview: ${e.message}")
                }
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                _cameraState.value = CameraState.Error("Failed to configure camera session")
            }
        }, cameraHandler)
    }

    fun captureImage(callback: (ByteArray) -> Unit) {
        onImageCaptured = callback
        try {
            val requestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            imageReader?.surface?.let { requestBuilder?.addTarget(it) }
            
            captureSession?.stopRepeating()
            captureSession?.abortCaptures()
            captureSession?.capture(requestBuilder?.build()!!, object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    createCaptureSession() // Restart preview
                }
            }, cameraHandler)
        } catch (e: CameraAccessException) {
            _cameraState.value = CameraState.Error("Failed to capture image: ${e.message}")
        }
    }

    fun closeCamera() {
        try {
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
            _cameraState.value = CameraState.Closed
        } catch (e: Exception) {
            Log.e("OcrCameraManager", "Error closing camera", e)
        }
    }

    fun release() {
        closeCamera()
        cameraThread.quitSafely()
        cameraExecutor.shutdown()
    }
}

sealed class CameraState {
    object Closed : CameraState()
    object Opened : CameraState()
    data class Error(val message: String) : CameraState()
} 