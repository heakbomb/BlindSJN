package com.glowstudio.android.blindsjn.feature.certification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton

/**
 * 사업자 인증 정보를 입력받는 UI 화면을 표시합니다.
 *
 * 사용자는 전화번호, 사업자 인증번호, 업종을 입력 및 선택할 수 있으며, "인증" 버튼을 눌러 입력값을 전달할 수 있습니다.
 *
 * @param onConfirm 사용자가 인증 버튼을 눌렀을 때 입력된 전화번호, 인증번호, 업종을 전달하는 콜백 함수
 */
@Composable
fun BusinessCertificationScreen(
    navController: NavController,
    onConfirm: (phone: String, certNumber: String, industry: String) -> Unit
) {
    // 상태 변수 선언
    var phone by remember { mutableStateOf("") }
    var certNumber by remember { mutableStateOf("") }
    // 업종 선택: 드롭다운 메뉴를 위한 상태
    val industries = listOf("음식점 및 카페", "쇼핑 및 리테일", "건강 및 의료", "숙박 및 여행",
                            "교육 및 학습", "여가 및 오락", "금융 및 공공기관")
    var selectedIndustry by remember { mutableStateOf(industries.first()) }
    var expanded by remember { mutableStateOf(false) }

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
            onValueChange = { phone = it },
            label = { Text("전화번호") },
            modifier = Modifier.fillMaxWidth()
        )

        // 사업자 인증번호 입력
        OutlinedTextField(
            value = certNumber,
            onValueChange = { certNumber = it },
            label = { Text("사업자 인증번호") },
            modifier = Modifier.fillMaxWidth()
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

        // 인증 확인 버튼
        CommonButton(
            text = "인증",
            onClick = { onConfirm(phone, certNumber, selectedIndustry) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
