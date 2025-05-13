package com.glowstudio.android.blindsjn.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    // String 형태의 날짜를 파싱해서 “~ 전” 표시를 돌려줍니다.
    fun getTimeAgo(dateStr: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = try {
            dateFormat.parse(dateStr)
        } catch (e: Exception) {
            null
        } ?: return dateStr

        val now = Date()
        val diff = now.time - date.time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours   = minutes / 60
        val days    = hours / 24

        return when {
            seconds < 60       -> "방금 전"
            minutes < 60       -> "${minutes}분 전"
            hours < 24         -> "${hours}시간 전"
            days < 7           -> "${days}일 전"
            else               -> SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                .format(date)
        }
    }
}
