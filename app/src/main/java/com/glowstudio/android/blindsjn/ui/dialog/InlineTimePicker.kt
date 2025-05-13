package com.glowstudio.android.blindsjn.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.ui.components.common.CommonButton

/**
 * 인라인 형태의 시간 선택 UI를 표시하는 컴포저블 함수입니다.
 *
 * 초기 시간과 분을 설정할 수 있으며, 사용자가 시간을 선택하고 "확인" 버튼을 누르면 선택된 시간과 분이 콜백으로 전달됩니다.
 *
 * @param initialHour 초기 시간(0~23), 기본값은 13입니다.
 * @param initialMinute 초기 분(0~59), 기본값은 0입니다.
 * @param onTimeSelected 사용자가 시간을 선택하고 확인 버튼을 누를 때 호출되는 콜백. 선택된 시간과 분이 인자로 전달됩니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InlineTimePicker(
    initialHour: Int = 13,
    initialMinute: Int = 0,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    // TimePicker 상태 생성
    val timePickerState = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 인라인 TimePicker 표시
        TimePicker(state = timePickerState)
        // 선택 완료 버튼
        CommonButton(
            text = "확인",
            onClick = { onTimeSelected(timePickerState.hour, timePickerState.minute) },
            modifier = Modifier.align(Alignment.End)
        )
    }
}
