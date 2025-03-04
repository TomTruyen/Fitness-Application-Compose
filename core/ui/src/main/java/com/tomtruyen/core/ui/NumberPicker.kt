package com.tomtruyen.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private fun <T> getItemIndexForOffset(
    range: List<T>,
    value: T,
    offset: Float,
    halfNumbersColumnHeightPx: Float
): Int {
    val indexOf = range.indexOf(value) - (offset / halfNumbersColumnHeightPx).toInt()
    return maxOf(0, minOf(indexOf, range.count() - 1))
}

@Composable
fun <T> NumberPicker(
    modifier: Modifier = Modifier,
    label: (T) -> String = { it.toString() },
    value: T,
    enabled: Boolean = true,
    onValueChange: (T) -> Unit,
    dividersColor: Color = MaterialTheme.colorScheme.primary,
    list: List<T>,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val totalVisibleItems = 3
    val minScale = 0.7f // Minimum scaling factor
    val maxScale = 1.25f // Maximum scaling factor for the middle label
    val minimumAlpha = 0.3f
    val verticalMargin = 8.dp
    val numbersColumnHeight = 80.dp * maxScale
    val halfNumbersColumnHeight = numbersColumnHeight / 2
    val halfNumbersColumnHeightPx = with(LocalDensity.current) { halfNumbersColumnHeight.toPx() }

    val coroutineScope = rememberCoroutineScope()

    val animatedOffset = remember { Animatable(0f) }
        .apply {
            val index = list.indexOf(value)
            val offsetRange = remember(value, list) {
                -((list.count() - 1) - index) * halfNumbersColumnHeightPx to
                        index * halfNumbersColumnHeightPx
            }
            updateBounds(offsetRange.first, offsetRange.second)
        }

    val coercedAnimatedOffset = animatedOffset.value % halfNumbersColumnHeightPx

    val indexOfElement =
        getItemIndexForOffset(list, value, animatedOffset.value, halfNumbersColumnHeightPx)

    var dividersWidth by remember { mutableStateOf(0.dp) }

    Layout(
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { deltaY ->
                                coroutineScope.launch {
                                    animatedOffset.snapTo(animatedOffset.value + deltaY)
                                }
                            },
                            onDragStopped = { velocity ->
                                coroutineScope.launch {
                                    val endValue = animatedOffset.fling(
                                        initialVelocity = velocity,
                                        animationSpec = exponentialDecay(frictionMultiplier = 1f),
                                        adjustTarget = { target ->
                                            val coercedTarget = target % halfNumbersColumnHeightPx
                                            val coercedAnchors =
                                                listOf(
                                                    -halfNumbersColumnHeightPx,
                                                    0f,
                                                    halfNumbersColumnHeightPx
                                                )
                                            val coercedPoint =
                                                coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
                                            val base =
                                                halfNumbersColumnHeightPx * (target / halfNumbersColumnHeightPx).toInt()
                                            coercedPoint + base
                                        }
                                    ).endState.value

                                    val result = list.elementAt(
                                        getItemIndexForOffset(
                                            list,
                                            value,
                                            endValue,
                                            halfNumbersColumnHeightPx
                                        )
                                    )
                                    onValueChange(result)
                                    animatedOffset.snapTo(0f)
                                }
                            }
                        )
                        .alpha(1f)
                } else {
                    Modifier.alpha(0.5f)
                }
            )

            .padding(vertical = numbersColumnHeight / totalVisibleItems + verticalMargin * 2),
        content = {
            Box(
                modifier
                    .width(dividersWidth)
                    .height(1.dp)
                    .background(color = dividersColor)
            )
            Box(
                modifier = Modifier
                    .padding(vertical = verticalMargin, horizontal = 20.dp)
                    .offset { IntOffset(x = 0, y = coercedAnimatedOffset.roundToInt()) }
            ) {
                val baseLabelModifier = Modifier
                    .height(numbersColumnHeight.div(totalVisibleItems))
                    .align(Alignment.Center)
                val scalingRange = maxScale - minScale
                val scrollProgress = abs(coercedAnimatedOffset) / halfNumbersColumnHeightPx
                val middleLabelScale = maxScale - scalingRange * scrollProgress

                if (indexOfElement > 0) {
                    val topLabelScale = when {
                        coercedAnimatedOffset > 0 -> minScale + (1 - minScale) * scrollProgress * totalVisibleItems
                        coercedAnimatedOffset < 0 -> minScale - (1 + minScale) * scrollProgress
                        else -> 1f
                    }

                    Label(
                        text = label(list.elementAt(indexOfElement - 1)),
                        style = textStyle,
                        modifier = baseLabelModifier
                            .offset(y = -halfNumbersColumnHeight)
                            .alpha(
                                maxOf(
                                    minimumAlpha,
                                    coercedAnimatedOffset / halfNumbersColumnHeightPx
                                )
                            ),
                        labelScale = topLabelScale
                    )
                }
                Label(
                    text = label(list.elementAt(indexOfElement)),
                    style = textStyle.copy(
                        fontSize = textStyle.fontSize * maxScale,
                    ),
                    modifier = baseLabelModifier,
                    labelScale = middleLabelScale
                )
                if (indexOfElement < list.count() - 1) {
                    val bottomLabelScale = when {
                        coercedAnimatedOffset > 0 -> minScale - (1 + minScale) * scrollProgress
                        coercedAnimatedOffset < 0 -> minScale + (1 - minScale) * scrollProgress * totalVisibleItems
                        else -> 1f
                    }

                    Label(
                        text = label(list.elementAt(indexOfElement + 1)),
                        style = textStyle,
                        modifier = baseLabelModifier
                            .offset(y = halfNumbersColumnHeight)
                            .alpha(
                                maxOf(
                                    minimumAlpha,
                                    -coercedAnimatedOffset / halfNumbersColumnHeightPx
                                )
                            ),
                        labelScale = bottomLabelScale
                    )
                }
            }
            Box(
                modifier
                    .width(dividersWidth)
                    .height(1.dp)
                    .background(color = dividersColor)
            )
        }
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        dividersWidth = placeables
            .drop(1)
            .first()
            .width
            .toDp()

        // Set the size of the layout as big as it can
        layout(
            dividersWidth.toPx().toInt(), placeables
                .sumOf {
                    it.height
                }
        ) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->

                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Composable
private fun springBackEasing(x: Float): Float {
    // You can adjust the damping ratio and stiffness as needed for your desired easing effect
    return animateFloatAsState(
        targetValue = x, animationSpec = spring(dampingRatio = 0.8f, stiffness = 100f),
        label = ""
    ).value
}

@Composable
private fun springForwardEasing(x: Float): Float {
    // You can adjust the damping ratio and stiffness as needed for your desired easing effect
    return animateFloatAsState(
        targetValue = x, animationSpec = spring(dampingRatio = 0.8f, stiffness = 100f),
        label = ""
    ).value
}

@Composable
private fun Label(text: String, style: TextStyle, labelScale: Float, modifier: Modifier) {
    Text(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                // Empty to disable text selection
            })
        },
        text = text,
        style = style.copy(fontSize = style.fontSize * labelScale),
        textAlign = TextAlign.Center,
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    animationSpec: DecayAnimationSpec<Float>,
    adjustTarget: ((Float) -> Float)?,
    block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null,
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = animationSpec.calculateTargetValue(value, initialVelocity)
    val adjustedTarget = adjustTarget?.invoke(targetValue)
    return if (adjustedTarget != null) {
        animateTo(
            targetValue = adjustedTarget,
            initialVelocity = initialVelocity,
            block = block
        )
    } else {
        animateDecay(
            initialVelocity = initialVelocity,
            animationSpec = animationSpec,
            block = block,
        )
    }
}
