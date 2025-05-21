package com.glowstudio.android.blindsjn.feature.certification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton

@Composable
fun BusinessCertificationScreen(
    navController: NavController,
    viewModel: BusinessCertViewModel,
    userId: Int,
    onConfirm: () -> Unit
) {
    // 상태 변수 선언
    var phone by remember { mutableStateOf("") }
    var certNumber by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var certNumberError by remember { mutableStateOf<String?>(null) }
    
    // 업종 선택: 드롭다운 메뉴를 위한 상태
    val industries = listOf("음식점 및 카페", "쇼핑 및 리테일", "건강 및 의료", "숙박 및 여행",
                            "교육 및 학습", "여가 및 오락", "금융 및 공공기관")
    var selectedIndustry by remember { mutableStateOf(industries.first()) }
    var expanded by remember { mutableStateOf(false) }

    // 업종 ID 매핑
    val industryIdMap = mapOf(
        "음식점 및 카페" to 1,
        "쇼핑 및 리테일" to 2,
        "건강 및 의료" to 3,
        "숙박 및 여행" to 4,
        "교육 및 학습" to 5,
        "여가 및 오락" to 6,
        "금융 및 공공기관" to 7
    )

    // 전화번호 포맷팅 함수
    fun formatPhoneNumber(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.length <= 3 -> digits
            digits.length <= 7 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
            else -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7, minOf(digits.length, 11))}"
        }
    }

    // 사업자등록번호 포맷팅 함수
    fun formatBusinessNumber(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.length <= 3 -> digits
            digits.length <= 5 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
            else -> "${digits.substring(0, 3)}-${digits.substring(3, 5)}-${digits.substring(5, minOf(digits.length, 10))}"
        }
    }

    // 입력값 검증 함수
    fun validateInputs(): Boolean {
        var isValid = true
        
        // 전화번호 검증
        val phoneDigits = phone.filter { it.isDigit() }
        if (phoneDigits.length != 11) {
            phoneError = "올바른 전화번호를 입력해주세요"
            isValid = false
        } else {
            phoneError = null
        }

        // 사업자등록번호 검증
        val certDigits = certNumber.filter { it.isDigit() }
        if (certDigits.length != 10) {
            certNumberError = "올바른 사업자등록번호를 입력해주세요"
            isValid = false
        } else {
            certNumberError = null
        }

        return isValid
    }

    // 성공 시 자동으로 이전 화면으로 이동
    LaunchedEffect(viewModel.isSuccess.value) {
        if (viewModel.isSuccess.value) {
            onConfirm()
        }
    }

    // 화면을 벗어날 때 상태 초기화
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    // 전체 레이아웃
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "사업자 인증",
            style = MaterialTheme.typography.titleLarge
        )

        // 전화번호 입력
        OutlinedTextField(
            value = phone,
            onValueChange = { 
                phone = formatPhoneNumber(it)
                phoneError = null
            },
            label = { Text("전화번호") },
            modifier = Modifier.fillMaxWidth(),
            isError = phoneError != null,
            supportingText = { phoneError?.let { Text(it) } },
            placeholder = { Text("010-0000-0000") }
        )

        // 사업자 인증번호 입력
        OutlinedTextField(
            value = certNumber,
            onValueChange = { 
                certNumber = formatBusinessNumber(it)
                certNumberError = null
            },
            label = { Text("사업자 인증번호") },
            modifier = Modifier.fillMaxWidth(),
            isError = certNumberError != null,
            supportingText = { certNumberError?.let { Text(it) } },
            placeholder = { Text("000-00-00000") }
        )

        // 업종 선택: 드롭다운 메뉴 사용
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedIndustry,
                onValueChange = {},
                label = { Text("업종 선택") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                enabled = false,
                readOnly = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                industries.forEach { industry ->
                    DropdownMenuItem(
                        text = { Text(industry) },
                        onClick = {
                            selectedIndustry = industry
                            expanded = false
                        }
                    )
                }
            }
        }

        // 결과 메시지 표시
        if (viewModel.resultMessage.value.isNotEmpty()) {
            Text(
                text = viewModel.resultMessage.value,
                color = if (viewModel.resultMessage.value.contains("완료")) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error
            )
        }

        // 인증 확인 버튼
        CommonButton(
            text = "인증",
            onClick = { 
                if (validateInputs()) {
                    viewModel.onBusinessCertClick(
                        userId = userId,
                        phoneNumber = phone.filter { it.isDigit() },
                        businessNumber = certNumber.filter { it.isDigit() },
                        industryId = industryIdMap[selectedIndustry] ?: 1
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading.value
        )

        // 로딩 표시
        if (viewModel.isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
