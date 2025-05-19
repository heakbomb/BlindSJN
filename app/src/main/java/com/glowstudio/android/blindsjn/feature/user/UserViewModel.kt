package com.glowstudio.android.blindsjn.feature.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.data.session.getUserIdFlow
import com.glowstudio.android.blindsjn.data.session.saveUserId
import com.glowstudio.android.blindsjn.data.session.clearUserId
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    // userId를 StateFlow로 관리
    val userIdFlow: StateFlow<Int> = getUserIdFlow(context).stateIn(
        viewModelScope, SharingStarted.Eagerly, -1
    )

    fun saveUserId(userId: Int) {
        viewModelScope.launch { saveUserId(context, userId) }
    }

    fun clearUserId() {
        viewModelScope.launch { clearUserId(context) }
    }
} 