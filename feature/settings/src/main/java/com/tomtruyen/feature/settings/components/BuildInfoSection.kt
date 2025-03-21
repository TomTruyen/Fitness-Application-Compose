package com.tomtruyen.feature.settings.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.common.providers.BuildConfigFieldProvider
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.feature.settings.R
import org.koin.compose.koinInject

@Composable
fun ColumnScope.BuildInfoSection() {
    val buildConfigFieldProvider = koinInject<BuildConfigFieldProvider>()

    Text(
        text = stringResource(
            id = R.string.label_version_and_build,
            buildConfigFieldProvider.versionName,
            buildConfigFieldProvider.versionCode
        ),
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = Dimens.Small)
            .padding(horizontal = Dimens.Normal)
    )
}