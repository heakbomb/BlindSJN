package com.glowstudio.android.blindsjn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glowstudio.android.blindsjn.ui.navigation.AppNavHost
import com.glowstudio.android.blindsjn.feature.main.viewmodel.TopBarViewModel
import com.glowstudio.android.blindsjn.ui.theme.BlindSJNTheme

class MainActivity : ComponentActivity() {
    /**
     * 액티비티 생성 시 엣지 투 엣지 모드를 활성화하고 Jetpack Compose를 사용해 앱의 UI와 내비게이션을 설정합니다.
     *
     * @param savedInstanceState 이전 상태가 저장된 번들 또는 null
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlindSJNTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val topBarViewModel: TopBarViewModel = viewModel()
                    AppNavHost(
                        navController = navController
                    )
                }
            }
        }
    }
}

