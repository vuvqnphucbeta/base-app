package com.beta.soft.component.anim

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.min

@Composable
fun Modifier.animateScaleIn(
    initialScale: Float = 0f,
    timeDelay: Long = 0L
): Modifier {
    var targetScale by remember { mutableFloatStateOf(initialScale) }
    LaunchedEffect(Unit) {
        delay(timeDelay)
        targetScale = 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 100f
        ),
        label = "ScaleAnimation"
    )

    return this.then(Modifier.scale(scale))
}

@Composable
fun Modifier.animatePulse(
    minScale: Float = 0.9f,
    maxScale: Float = 1.1f,
    duration: Int = 800
): Modifier {

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    return this.scale(scale)
}

@Composable
fun Modifier.animateSlideInBottom(
    initialOffsetY: Float = 200f, // Khoảng cách bắt đầu từ dưới lên (pixel)
    timeDelay: Long = 0L,
    dampingRatio: Float = 0.7f,
    stiffness: Float = 100f,
    enabled: Boolean = true,
): Modifier {
    if (!enabled) return this
    var targetOffsetY by remember { mutableFloatStateOf(initialOffsetY) }

    LaunchedEffect(Unit) {
        delay(timeDelay)
        targetOffsetY = 0f // Di chuyển về vị trí gốc
    }

    val animatedOffset by animateFloatAsState(
        targetValue = targetOffsetY,
        animationSpec = spring(
            dampingRatio = dampingRatio,
            stiffness = stiffness
        ),
        label = "AnimateSlideInBottom"
    )

    return this.then(
        Modifier.graphicsLayer {
            // ModulateAlpha: xem ghi chú đầu file — tránh layer cắt mất drop shadow.
            compositingStrategy = CompositingStrategy.ModulateAlpha
            translationY = animatedOffset
            alpha = 1f - (animatedOffset / initialOffsetY).coerceIn(0f, 1f)
        }
    )
}


@Composable
fun Modifier.animateSlideInRight(
    initialOffsetX: Float = 200f,
    timeDelay: Long = 0L
): Modifier {
    var targetOffsetX by remember { mutableFloatStateOf(initialOffsetX) }

    LaunchedEffect(Unit) {
        delay(timeDelay)
        targetOffsetX = 0f // Di chuyển về vị trí gốc
    }

    val animatedOffset by animateFloatAsState(
        targetValue = targetOffsetX,
        animationSpec = spring(
            dampingRatio = 0.7f, // Độ nảy (0.7 là mức cân bằng)
            stiffness = 100f     // Độ cứng của lò xo
        ),
        label = "AnimateSlideInRight"
    )

    // Sử dụng graphicsLayer để tối ưu hiệu năng animation
    return this.then(
        Modifier.graphicsLayer {
            // ModulateAlpha: xem ghi chú đầu file — tránh layer cắt mất drop shadow.
            compositingStrategy = CompositingStrategy.ModulateAlpha
            translationX = animatedOffset
            alpha = 1f - (animatedOffset / initialOffsetX).coerceIn(0f, 1f)
        }
    )
}

@Composable
fun Modifier.animateSlideInLeft(
    initialOffsetX: Float = 200f,
    timeDelay: Long = 0L
): Modifier {

    var targetOffsetX by remember { mutableFloatStateOf(-initialOffsetX) }

    LaunchedEffect(Unit) {
        delay(timeDelay)
        targetOffsetX = 0f
    }

    val animatedOffset by animateFloatAsState(
        targetValue = targetOffsetX,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 100f
        ),
        label = "AnimateSlideInLeft"
    )

    return this.then(
        Modifier.graphicsLayer {
            // ModulateAlpha: xem ghi chú đầu file — tránh layer cắt mất drop shadow.
            compositingStrategy = CompositingStrategy.ModulateAlpha
            translationX = animatedOffset
            alpha = 1f - (abs(animatedOffset) / initialOffsetX)
                .coerceIn(0f, 1f)
        }
    )
}

@Composable
fun Modifier.animateBounceEverySecond(
    bounceHeight: Float = 24f
): Modifier {

    val infiniteTransition = rememberInfiniteTransition(label = "bounce")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -bounceHeight,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0f at 0
                bounceHeight at 200
                0f at 400
                0f at 1000
            }
        ),
        label = "bounceOffset"
    )

    return this.then(
        Modifier.graphicsLayer {
            translationY = offsetY
        }
    )
}
private val animatedSlideInIndices =
    java.util.Collections.synchronizedSet(mutableSetOf<String>())
@Composable
fun Modifier.animateSlideInItem(
    index: Int,
    initialOffsetY: Float = 100f,
    dampingRatio: Float = 0.6f,
    stiffness: Float = 200f,
    baseDelay: Long = 0L,
    stepDelay: Long = 20L,
    maxStepDelay: Long = 80L,
    groupKey: String = "",
    delayIndex: Int = index,
): Modifier {
    val hasAnimated = remember(groupKey, index) {
        !animatedSlideInIndices.add("$groupKey#$index")
    }

    if (hasAnimated) return this

    val timeDelay = remember(index) { baseDelay + min(delayIndex * stepDelay, maxStepDelay) }
    var targetOffsetY by remember { mutableFloatStateOf(initialOffsetY) }

    LaunchedEffect(Unit) {
        delay(timeDelay)
        targetOffsetY = 0f
    }

    val animatedOffset by animateFloatAsState(
        targetValue = targetOffsetY,
        animationSpec = spring(
            dampingRatio = dampingRatio,
            stiffness = stiffness
        ),
        label = "SlideInItem"
    )

    return this.then(
        Modifier.graphicsLayer {
            compositingStrategy = CompositingStrategy.ModulateAlpha
            translationY = animatedOffset
            alpha = 1f - (animatedOffset / initialOffsetY).coerceIn(0f, 1f)
        }
    )
}

@Composable
fun Modifier.animateSlideInTop(
    initialOffsetY: Float = -200f, // Bắt đầu từ phía trên
    timeDelay: Long = 0L
): Modifier {
    var targetOffsetY by remember { mutableFloatStateOf(initialOffsetY) }

    LaunchedEffect(Unit) {
        delay(timeDelay)
        targetOffsetY = 0f
    }

    val animatedOffset by animateFloatAsState(
        targetValue = targetOffsetY,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 100f
        ),
        label = "AnimateSlideInTop"
    )

    return this.then(
        Modifier.graphicsLayer {
            // ModulateAlpha: xem ghi chú đầu file — tránh layer cắt mất drop shadow.
            compositingStrategy = CompositingStrategy.ModulateAlpha
            translationY = animatedOffset
            alpha = 1f - (abs(animatedOffset) / abs(initialOffsetY))
                .coerceIn(0f, 1f)
        }
    )
}