package com.tomtruyen.core.ui.swipereveal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.tomtruyen.core.designsystem.Dimens
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeToRevealBox(
    state: SwipeToRevealState,
    actions: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    var contextMenuWidth by remember(state.key) {
        mutableFloatStateOf(0f)
    }
    val offset = remember(state.key) {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.key, state.isRevealed.value, contextMenuWidth) {
        if (state.isRevealed.value) {
            offset.animateTo(
                targetValue = -contextMenuWidth,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        } else {
            offset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onSizeChanged {
                    contextMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(enabled, state.isRevealed.value, state.key, contextMenuWidth) {
                    if (enabled) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, dragAmount ->
                                scope.launch {
                                    val newOffset = (offset.value + dragAmount)
                                        .coerceIn(-contextMenuWidth, 0f)
                                    offset.snapTo(newOffset)
                                }
                            },
                            onDragEnd = {
                                val threshold = if(state.isRevealed.value) {
                                    1 - state.swipeThreshold
                                } else {
                                    state.swipeThreshold
                                }

                                when {
                                    offset.value <= -contextMenuWidth * threshold -> {
                                        scope.launch {
                                            offset.animateTo(
                                                targetValue = -contextMenuWidth,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                            state.expand()
                                        }
                                    }

                                    else -> {
                                        scope.launch {
                                            offset.animateTo(
                                                targetValue = 0f,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                            state.collapse()
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
        ) {
            content()
        }
    }
}

@Composable
fun SwipeToRevealAction(
    icon: ImageVector,
    contentColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    text: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = Dimens.Small,
                    bottomStart = Dimens.Small
                )
            )
            .padding(horizontal = Dimens.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny, Alignment.End)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = contentColor,
        )

        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = contentColor,
                    fontWeight = FontWeight.W500
                ),
            )
        }
    }
}

@Composable
fun rememberSwipeToRevealState(
    key: Any,
    swipeThreshold: Float = 0.25f // Higher the number (to 1) the faster it will reveal (less swiping needed)
): SwipeToRevealState {
    val isRevealed = remember(key) { mutableStateOf(false) }

    return remember(key) {
        SwipeToRevealState(
            key = key,
            isRevealed = isRevealed,
            swipeThreshold = swipeThreshold.coerceIn(0f, 1f),
            expand = { isRevealed.value = true },
            collapse = { isRevealed.value = false }
        )
    }
}

class SwipeToRevealState(
    val key: Any,
    val isRevealed: MutableState<Boolean>,
    val swipeThreshold: Float,
    val expand: () -> Unit,
    val collapse: () -> Unit
)