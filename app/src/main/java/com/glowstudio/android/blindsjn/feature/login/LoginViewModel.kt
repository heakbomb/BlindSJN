package com.glowstudio.android.blindsjn.feature.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.data.network.AuthRepository
import com.glowstudio.android.blindsjn.data.network.AutoLoginManager
import com.glowstudio.android.blindsjn.data.network.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val phoneNumber: String = "",
    val password: String = "",
    val autoLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val showEmptyFieldsPopup: Boolean = false,
    val showInvalidCredentialsPopup: Boolean = false,
    val showNetworkErrorPopup: Boolean = false
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var onLoginSuccess: ((Boolean) -> Unit)? = null

    /**
     * 로그인 성공 시 호출할 콜백 함수를 설정합니다.
     *
     * @param callback 로그인 성공 여부를 인자로 받는 콜백 함수
     */
    fun setOnLoginSuccess(callback: (Boolean) -> Unit) {
        onLoginSuccess = callback
    }

    /**
     * 입력된 전화번호에서 숫자만 추출하여 UI 상태의 전화번호를 업데이트합니다.
     *
     * @param phoneNumber 사용자가 입력한 전화번호 문자열
     */
    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = phoneNumber.filter { it.isDigit() }
        )
    }

    /**
     * UI 상태의 비밀번호 값을 업데이트합니다.
     *
     * @param password 새로운 비밀번호 문자열
     */
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    /**
     * 자동 로그인 활성화 여부를 UI 상태에 반영합니다.
     *
     * @param enabled 자동 로그인 기능의 활성화 여부
     */
    fun updateAutoLogin(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoLoginEnabled = enabled)
    }

    /**
     * 자동 로그인 설정을 확인하고 저장된 자격 증명이 있으면 자동 로그인을 시도합니다.
     *
     * 네트워크가 연결되어 있지 않으면 네트워크 오류 팝업을 표시합니다.
     * 자동 로그인이 활성화되어 있고 저장된 자격 증명이 존재할 경우, 해당 정보를 사용해 로그인 시도를 진행합니다.
     * 로그인 성공 시 onLoginSuccess 콜백을 호출하며, 실패 또는 예외 발생 시 잘못된 자격 증명 팝업을 표시합니다.
     */
    fun checkAutoLogin(context: Context) {
        viewModelScope.launch {
            if (!isNetworkAvailable(context)) {
                _uiState.value = _uiState.value.copy(showNetworkErrorPopup = true)
                return@launch
            }

            val autoLoginEnabled = AutoLoginManager.isAutoLoginEnabled(context)
            _uiState.value = _uiState.value.copy(autoLoginEnabled = autoLoginEnabled)

            if (autoLoginEnabled) {
                AutoLoginManager.getSavedCredentials(context)?.let { (savedPhone, savedPassword) ->
                    _uiState.value = _uiState.value.copy(
                        phoneNumber = savedPhone,
                        password = savedPassword,
                        isLoading = true
                    )
                    try {
                        val success = AuthRepository.login(context, savedPhone, savedPassword)
                        if (success) {
                            onLoginSuccess?.invoke(true)
                        } else {
                            _uiState.value = _uiState.value.copy(showInvalidCredentialsPopup = true)
                        }
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(showInvalidCredentialsPopup = true)
                    } finally {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    /**
     * 사용자의 전화번호와 비밀번호로 로그인을 시도합니다.
     *
     * 입력값이 비어 있거나 네트워크 연결이 없을 경우 각각 알림 팝업을 표시합니다.
     * 로그인 성공 시 자동 로그인 정보를 저장하고, 성공 콜백을 호출합니다.
     * 로그인 실패 또는 예외 발생 시 잘못된 자격 증명 알림 팝업을 표시합니다.
     */
    fun login(context: Context, phoneNumber: String, password: String) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            if (phoneNumber.isEmpty() || password.isEmpty()) {
                _uiState.value = _uiState.value.copy(showEmptyFieldsPopup = true)
                return@launch
            }

            if (!isNetworkAvailable(context)) {
                _uiState.value = _uiState.value.copy(showNetworkErrorPopup = true)
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val success = AuthRepository.login(context, phoneNumber, password)
                if (success) {
                    AutoLoginManager.saveLoginInfo(
                        context,
                        phoneNumber,
                        password,
                        _uiState.value.autoLoginEnabled
                    )
                    onLoginSuccess?.invoke(true)
                } else {
                    _uiState.value = _uiState.value.copy(showInvalidCredentialsPopup = true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(showInvalidCredentialsPopup = true)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * 빈 입력 필드 알림 팝업을 닫습니다.
     */
    fun dismissEmptyFieldsPopup() {
        _uiState.value = _uiState.value.copy(showEmptyFieldsPopup = false)
    }

    /**
     * 잘못된 자격 증명 팝업을 닫습니다.
     *
     * 로그인 시도 후 표시된 잘못된 자격 증명 알림을 숨깁니다.
     */
    fun dismissInvalidCredentialsPopup() {
        _uiState.value = _uiState.value.copy(showInvalidCredentialsPopup = false)
    }

    /**
     * 네트워크 오류 팝업을 닫습니다.
     *
     * UI 상태에서 네트워크 오류 팝업 표시 플래그를 해제합니다.
     */
    fun dismissNetworkErrorPopup() {
        _uiState.value = _uiState.value.copy(showNetworkErrorPopup = false)
    }
}