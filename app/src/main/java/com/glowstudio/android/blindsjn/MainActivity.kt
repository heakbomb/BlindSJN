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
     * 액티비티가 생성될 때 초기화 작업과 Jetpack Compose 기반 UI를 설정합니다.
     *
     * 엣지 투 엣지 디스플레이를 활성화하고, 앱의 테마와 네비게이션 컨트롤러를 적용하여 메인 화면을 구성합니다.
     *
     * @param savedInstanceState 이전 상태가 저장된 번들. 상태 복원 시 사용됩니다.
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

