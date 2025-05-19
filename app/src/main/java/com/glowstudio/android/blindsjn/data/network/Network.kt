package com.glowstudio.android.blindsjn.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

// ✅ 네트워크 상태 확인 함수
fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
