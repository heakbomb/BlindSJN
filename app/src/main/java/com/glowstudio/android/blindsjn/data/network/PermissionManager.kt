package com.glowstudio.android.blindsjn.data.network

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PermissionManager {
    private const val PREF_NAME = "permission_prefs"
    private const val KEY_CAMERA_PERMISSION = "camera_permission"

    private val _hasCameraPermission = MutableStateFlow(false)
    val hasCameraPermission: StateFlow<Boolean> = _hasCameraPermission.asStateFlow()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveCameraPermission(context: Context, granted: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_CAMERA_PERMISSION, granted).apply()
        _hasCameraPermission.value = granted
    }

    fun getCameraPermission(context: Context): StateFlow<Boolean> {
        val savedPermission = getPrefs(context).getBoolean(KEY_CAMERA_PERMISSION, false)
        _hasCameraPermission.value = savedPermission
        return hasCameraPermission
    }

    fun clearPermissions(context: Context) {
        getPrefs(context).edit().clear().apply()
        _hasCameraPermission.value = false
    }
} 