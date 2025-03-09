package com.tomtruyen.core.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomtruyen.core.common.R
import com.tomtruyen.core.common.utils.ImageLoader
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.datastore.ThemePreferencesDatastore
import com.tomtruyen.core.designsystem.theme.fallbackImageBackground
import com.tomtruyen.core.designsystem.theme.isDarkTheme
import org.koin.compose.koinInject

@Composable
fun Avatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    val context = LocalContext.current
    val imageLoader: ImageLoader = koinInject()

    Box(
        modifier = modifier.size(size)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(Dimens.Tiny)
    ) {
        val themeMode by ThemePreferencesDatastore.themeMode.collectAsState(ThemePreferencesDatastore.Mode.SYSTEM)
        Log.d("@@@", "ThemeMode: $themeMode")

        AsyncImage(
            model = imageLoader.load(context, imageUrl),
            fallback = painterResource(id = R.drawable.ic_fallback),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            colorFilter = if(imageUrl == null) {
                ColorFilter.tint(
                    color = MaterialTheme.colorScheme.fallbackImageBackground
                )
            } else null
        )
    }
}