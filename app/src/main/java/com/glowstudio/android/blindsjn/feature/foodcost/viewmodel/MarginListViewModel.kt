package com.glowstudio.android.blindsjn.feature.foodcost.viewmodel

import androidx.compose.ui.graphics.Color
import com.glowstudio.android.blindsjn.feature.foodcost.model.MarginItem

class MarginListViewModel {
    val items = listOf(
        MarginItem("잠봉뵈르 샌드위치", 1350000, 900000, 35, Color(0xFF1A237E), Color(0xFF90CAF9)),
        MarginItem("쑥라떼", 832000, 600000, 25, Color(0xFF283593), Color(0xFFB3E5FC)),
        MarginItem("브라운 치즈 마카롱", 721000, 500000, 30, Color(0xFF42A5F5), Color(0xFFB3E5FC)),
        MarginItem("까눌레", 500000, 350000, 20, Color(0xFF90CAF9), Color(0xFFB3E5FC)),
        MarginItem("아이스 아메리카노", 350000, 200000, 15, Color(0xFFB3E5FC), Color(0xFFE3F2FD))
    )
} 