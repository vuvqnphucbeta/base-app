package com.beta.soft.component.anim

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.defaultShimmerTheme

fun createCustomTheme() = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = 2500,
            delayMillis = 500, easing = FastOutSlowInEasing
        )
    ),
    blendMode = BlendMode.SrcOver, rotation = 30f,
    shaderColors = listOf(
        Color.Transparent,
        Color(0xFFFFFFFF).copy(alpha = 0.5f),
        Color(0xFFFFFFFF).copy(alpha = 0.2f),
        Color.Transparent,
        Color(0xFFFFFFFF).copy(alpha = 0.2f),
        Color(0xFFFFFFFF).copy(alpha = 0.5f),
        Color.Transparent,
    ),
    shaderColorStops = listOf(
        0.0f, 0.3f,
        0.5f, 0.5f,
        0.5f, 0.7f,
        1.0f
    ),
    shimmerWidth = 40.dp
)