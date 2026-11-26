package com.beta.soft.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.DropShadowPainter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.dropShadow as composeDropShadow

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
    hollow: Boolean = false,
): Modifier {
    val shadow = Shadow(
        radius = blur,
        color = color,
        spread = spread,
        offset = DpOffset(offsetX, offsetY),
    )
    return if (hollow) hollowDropShadow(shape, shadow) else composeDropShadow(shape, shadow)
}

private fun Modifier.hollowDropShadow(shape: Shape, shadow: Shadow): Modifier = drawWithCache {
    val painter = DropShadowPainter(shape, shadow)
    val nodePath = shape.createOutline(size, layoutDirection, this).toPath()
    onDrawBehind {
        clipPath(nodePath, clipOp = ClipOp.Difference) {
            with(painter) { draw(size) }
        }
    }
}

private fun Outline.toPath(): Path = when (this) {
    is Outline.Generic -> path
    is Outline.Rounded -> Path().apply { addRoundRect(roundRect) }
    is Outline.Rectangle -> Path().apply { addRect(rect) }
}

fun Modifier.iconGradient(brush: Brush) = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcIn)
        }
    }
