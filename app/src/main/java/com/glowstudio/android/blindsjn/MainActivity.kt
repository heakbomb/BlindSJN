package com.glowstudio.android.blindsjn

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.ui.navigation.AppNavHost
import com.glowstudio.android.blindsjn.feature.main.viewmodel.TopBarViewModel
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme
import android.content.pm.PackageManager
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    private var showPermissionDialog by mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showPermissionDialog = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlindSJNTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val topBarViewModel: TopBarViewModel = viewModel()
                    val context = LocalContext.current
                    
                    // 권한 요청 다이얼로그
                    if (showPermissionDialog) {
                        AlertDialog(
                            onDismissRequest = { showPermissionDialog = false },
                            title = { Text("카메라 권한 필요") },
                            text = { Text("영수증 촬영을 위해 카메라 권한이 필요합니다. 설정에서 권한을 허용해주세요.") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showPermissionDialog = false
                                        // 설정 화면으로 이동
                                        val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        intent.data = android.net.Uri.fromParts("package", context.packageName, null)
                                        context.startActivity(intent)
                                    }
                                ) {
                                    Text("설정으로 이동")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showPermissionDialog = false }
                                ) {
                                    Text("취소")
                                }
                            }
                        )
                    }

                    // 앱 시작 시 카메라 권한 확인 및 요청
                    LaunchedEffect(Unit) {
                        when {
                            context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                                // 권한이 이미 있는 경우
                            }
                            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                                // 권한이 거부된 적이 있는 경우
                                showPermissionDialog = true
                            }
                            else -> {
                                // 처음 권한을 요청하는 경우
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }

                    AppNavHost(
                        navController = navController
                    )
                }
            }
        }
    }
}

