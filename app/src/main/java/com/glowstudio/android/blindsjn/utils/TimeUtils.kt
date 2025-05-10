package com.glowstudio.android.blindsjn.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun getTimeAgo(dateStr: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(dateStr) ?: return dateStr

        val now = Calendar.getInstance().time
        val diffInMillis = now.time - date.time

        val seconds: Long = diffInMillis / 1000
        val minutes: Long = seconds / 60
        val hours: Long = minutes / 60
        val days: Long = hours / 24
        val weeks: Long = days / 7
        val months: Long = days / 30
        val years: Long = days / 365

        return when {
            seconds < 60L -> "방금 전"
            minutes < 60L -> "${minutes}분 전"
            hours < 24L -> "${hours}시간 전"
            days < 7L -> "${days}일 전"
            weeks < 4L -> "${weeks}주 전"
            months < 12L -> "${months}달 전"
            else -> "${years}년 전"
        }
    }
}