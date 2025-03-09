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
import androidx.compose.ui.Modifier
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelTimerPickerSheet(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    val state = rememberModalBottomSheetState()

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
                    modifier = Modifier.fillMaxWidth()
                )

                Buttons.Default(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Save"
                ) {
                    // TODO: Implement on Action
                }
            }

        }
    }
}