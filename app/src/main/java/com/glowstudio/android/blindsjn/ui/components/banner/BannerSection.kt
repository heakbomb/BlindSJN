package com.glowstudio.android.blindsjn.ui.components.banner


import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerSection() {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFF5F7FF))
    ) {
        HorizontalPager(
            count = 4,
            state = pagerState,
        ) { page ->
            Image(
                painter = painterResource(id = R.drawable.login_image),
                contentDescription = "배너 이미지 $page",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = Color.Gray.copy(alpha = 0.5f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // 시각적으로 광고 포함 높이를 맞춰줌
                .background(Color(0xFFF5F7FF)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color.Black))

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp), // 광고 자체 높이는 여기서 유지됨
                factory = { context ->
                    val adWidth = (context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density).toInt()
                    AdView(context).apply {
                        setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth))
                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color.Black))
        }
    }
}