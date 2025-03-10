package com.tomtruyen.feature.profile.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.utils.EmailUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.feature.profile.R

@Composable
fun ContactSection() {
    val context = LocalContext.current

    val emailLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Label(
        label = stringResource(id = R.string.label_contact_and_support),
        modifier = Modifier.padding(
            start = 12.dp,
            end = 12.dp,
            top = Dimens.Normal,
            bottom = Dimens.Tiny
        )
    )

    ListItem(
        title = stringResource(id = R.string.label_report_an_issue),
        message = stringResource(id = R.string.label_message_report_an_issue),
        onClick = {
            emailLauncher.launch(
                EmailUtils.getEmailIntent(context)
            )
        }
    )
}