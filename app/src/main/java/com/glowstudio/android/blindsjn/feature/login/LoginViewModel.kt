package com.glowstudio.android.blindsjn.feature.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.data.network.AuthRepository
import com.glowstudio.android.blindsjn.data.network.AutoLoginManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var onLoginSuccess: ((Boolean) -> Unit)? = null

    fun setOnLoginSuccess(callback: (Boolean) -> Unit) {
        onLoginSuccess = callback
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateAutoLogin(enabled: Boolean) {
        _uiState.update { it.copy(autoLoginEnabled = enabled) }
    }

    fun login(context: android.content.Context, phoneNumber: String, password: String) {
        if (phoneNumber.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(showEmptyFieldsPopup = true) }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = AuthRepository.login(phoneNumber, password)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val userId = response.body()?.userId ?: 1
                    _uiState.update { it.copy(userId = userId) }
                    if (_uiState.value.autoLoginEnabled) {
                        AutoLoginManager.saveLoginData(context, phoneNumber, password)
                    }
                    onLoginSuccess?.invoke(true)
                } else {
                    _uiState.update { it.copy(showInvalidCredentialsPopup = true) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(showNetworkErrorPopup = true) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun dismissEmptyFieldsPopup() {
        _uiState.update { it.copy(showEmptyFieldsPopup = false) }
    }

    fun dismissInvalidCredentialsPopup() {
        _uiState.update { it.copy(showInvalidCredentialsPopup = false) }
    }

    fun dismissNetworkErrorPopup() {
        _uiState.update { it.copy(showNetworkErrorPopup = false) }
    }
}

data class LoginUiState(
    val phoneNumber: String = "",
    val password: String = "",
    val autoLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val showEmptyFieldsPopup: Boolean = false,
    val showInvalidCredentialsPopup: Boolean = false,
    val showNetworkErrorPopup: Boolean = false,
    val userId: Int = 1
)