package com.tomtruyen.core.ui.wheeltimepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.R
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimerPickerSheet(
    seconds: Int,
    visible: Boolean,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    WheelTimerPickerSheet(
        initial = LocalTime.fromSecondOfDay(seconds),
        visible = visible,
        onSubmit = {
            onSubmit(it.toSecondOfDay())
        },
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelTimerPickerSheet(
    initial: LocalTime,
    visible: Boolean,
    onSubmit: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedTime by remember {
        mutableStateOf(initial)
    }

    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = state,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(Dimens.Normal),
                verticalArrangement = Arrangement.spacedBy(Dimens.Normal)
            ) {
                WheelTimePicker(
                    modifier = Modifier.fillMaxWidth(),
                    startTime = initial,
                    onSnappedTime = {
                        selectedTime = it
                    }
                )

                Buttons.Default(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.button_confirm),
                    onClick = {
                        onSubmit(selectedTime)

                        scope.launch {
                            state.hide()
                        }.invokeOnCompletion {
                            onDismiss()
                        }

                    }
                )
            }

        }
    }
}