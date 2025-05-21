package com.glowstudio.android.blindsjn.feature.certification

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.data.network.BusinessCertRepository
import kotlinx.coroutines.launch

class BusinessCertViewModel : ViewModel() {
    val resultMessage = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val isSuccess = mutableStateOf(false)

    fun onBusinessCertClick(
        userId: Int,
        phoneNumber: String,
        businessNumber: String,
        industryId: Int
    ) {
        viewModelScope.launch {
            isLoading.value = true
            isSuccess.value = false
            resultMessage.value = ""
            
            try {
                // 1. 사업자 등록번호 진위확인 API 호출
                val isValid = BusinessCertRepository.checkBusinessNumberValidity(businessNumber)

                if (isValid) {
                    // 2. 내부 API를 통해 인증 정보 저장
                    val saveResult = BusinessCertRepository.saveBusinessCertification(
                        userId = userId,
                        phoneNumber = phoneNumber,
                        businessNumber = businessNumber,
                        industryId = industryId
                    )

                    if (saveResult) {
                        resultMessage.value = "인증이 완료되었습니다."
                        isSuccess.value = true
                    } else {
                        resultMessage.value = "인증 정보 저장에 실패했습니다. 잠시 후 다시 시도해주세요."
                    }
                } else {
                    resultMessage.value = "유효하지 않은 사업자 등록번호입니다. 다시 확인해주세요."
                }
            } catch (e: Exception) {
                when (e) {
                    is java.net.UnknownHostException -> {
                        resultMessage.value = "인터넷 연결을 확인해주세요."
                    }
                    is java.net.SocketTimeoutException -> {
                        resultMessage.value = "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해주세요."
                    }
                    else -> {
                        resultMessage.value = "인증 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                    }
                }
            } finally {
                isLoading.value = false
            }
        }
    }

    fun resetState() {
        resultMessage.value = ""
        isLoading.value = false
        isSuccess.value = false
    }
}
