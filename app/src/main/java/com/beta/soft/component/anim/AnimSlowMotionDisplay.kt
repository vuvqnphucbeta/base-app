package com.beta.soft.component.anim

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AnimSlowMotionDisplay(
    index: Int,
    startAnimation: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {

    val animatedAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = index * 100,
            easing = FastOutSlowInEasing
        ), label = "alpha"
    )

    val animatedScale = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.85f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = index * 100,
            easing = FastOutSlowInEasing
        ), label = "scale"
    )

    content(
        modifier.graphicsLayer {
            alpha = animatedAlpha.value
            scaleX = animatedScale.value
            scaleY = animatedScale.value
        }
    )
}