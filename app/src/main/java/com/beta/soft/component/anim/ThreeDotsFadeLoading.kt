package com.beta.soft.component.anim

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ThreeDotsFadeLoading(
    modifier: Modifier = Modifier,
    dotColor: Color = Color(0xFF67CF98),
    dotSize: Dp = 12.dp,
    spacing: Dp = 8.dp,
    animationDuration: Int = 1200,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "DotsFadeTransition")

    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration / 2,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaAnimation"
    )

    Box(
        modifier = modifier
            .drawBehind {
                val dotRadiusPx = dotSize.toPx() / 2
                val spacingPx = spacing.toPx()
                val totalWidth = (dotSize.toPx() * 3) + (spacingPx * 2)
                val startX = (size.width - totalWidth) / 2
                val centerY = size.height / 2
                for (i in 0 until 3) {
                    val centerX = startX + dotRadiusPx + (i * (dotSize.toPx() + spacingPx))
                    val progressWithDelay = (alphaAnim - 0.2f) / (1f - 0.2f)
                    val dotDelay = i.toFloat() / 3f
                    var currentAlpha = 0.2f + (0.8f * ((progressWithDelay - dotDelay + 1f) % 1f))
                    currentAlpha = currentAlpha.coerceIn(0.2f, 1f)
                    drawCircle(
                        color = dotColor.copy(alpha = currentAlpha),
                        radius = dotRadiusPx,
                        center = Offset(centerX, centerY)
                    )
                }
            }
    )
}
