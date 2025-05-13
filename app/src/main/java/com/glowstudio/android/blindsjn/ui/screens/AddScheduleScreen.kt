package com.glowstudio.android.blindsjn.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.tempData.ScheduleInput
import com.glowstudio.android.blindsjn.ui.dialog.CustomTimePickerDialog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton

/**
 * 스케줄 항목을 추가할 수 있는 입력 화면을 표시하는 컴포저블입니다.
 *
 * 사용자는 제목, 시작일, 종료일, 시작 시간, 종료 시간, 메모를 입력할 수 있으며, 입력 완료 시 저장 또는 취소를 선택할 수 있습니다.
 *
 * @param onCancel 사용자가 취소를 선택할 때 호출됩니다.
 * @param onSave 사용자가 저장을 선택할 때 입력된 스케줄 정보를 포함하여 호출됩니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(
    onCancel: () -> Unit,
    onSave: (ScheduleInput) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showStartTimeDialog by remember { mutableStateOf(false) }
    var showEndTimeDialog by remember { mutableStateOf(false) }

    val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = null)
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("스케줄 추가") },
                actions = {
                    IconButton(onClick = {
                        val schedule = ScheduleInput(
                            title = title,
                            startDate = startDate,
                            endDate = endDate,
                            startTime = startTime,
                            endTime = endTime,
                            memo = memo
                        )
                        onSave(schedule)
                    }) {
                        Icon(Icons.Filled.Save, contentDescription = "저장")
                    }
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.Close, contentDescription = "취소")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth()
            )

            // 시작일/종료일 row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { },
                    label = { Text("시작일") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showStartDatePicker = true },
                    readOnly = true
                )
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { },
                    label = { Text("종료일") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showEndDatePicker = true },
                    readOnly = true
                )
            }

            // 시작시간/종료시간 row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { },
                    label = { Text("시작 시간") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showStartTimeDialog = true },
                    readOnly = true
                )
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { },
                    label = { Text("종료 시간") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showEndTimeDialog = true },
                    readOnly = true
                )
            }

            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                label = { Text("메모") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CommonButton(
                    text = "취소",
                    onClick = onCancel
                )
                Spacer(modifier = Modifier.width(8.dp))
                CommonButton(
                    text = "저장",
                    onClick = {
                        val schedule = ScheduleInput(
                            title = title,
                            startDate = startDate,
                            endDate = endDate,
                            startTime = startTime,
                            endTime = endTime,
                            memo = memo
                        )
                        onSave(schedule)
                    }
                )
            }
        }
    }

    // 시작일 DatePickerDialog
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = startDatePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val localDate: LocalDate = Instant.ofEpochMilli(selectedMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        startDate = localDate.toString()
                    }
                    showStartDatePicker = false
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    // 종료일 DatePickerDialog
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = endDatePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val localDate: LocalDate = Instant.ofEpochMilli(selectedMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        endDate = localDate.toString()
                    }
                    showEndDatePicker = false
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }

    // 시작 시간 TimePicker 팝업
    if (showStartTimeDialog) {
        CustomTimePickerDialog(
            onDismiss = { showStartTimeDialog = false },
            onConfirm = { hour, minute ->
                startTime = String.format("%02d:%02d", hour, minute)
                showStartTimeDialog = false
            }
        )
    }

    // 종료 시간 TimePicker 팝업
    if (showEndTimeDialog) {
        CustomTimePickerDialog(
            onDismiss = { showEndTimeDialog = false },
            onConfirm = { hour, minute ->
                endTime = String.format("%02d:%02d", hour, minute)
                showEndTimeDialog = false
            }
        )
    }
}
