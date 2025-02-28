package com.tomtruyen.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomtruyen.core.common.R
import com.tomtruyen.core.common.utils.ImageLoader
import org.koin.compose.koinInject

@Composable
fun Avatar(
    imageUrl: String?,
    modifier: Modifier  = Modifier,
    contentDescription: String? = null,
    size: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    val imageLoader: ImageLoader = koinInject()

    AsyncImage(
        model = imageLoader.load(imageUrl),
        fallback = painterResource(R.drawable.ic_fallback),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
    )
}