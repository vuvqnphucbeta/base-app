package com.beta.soft.component.anim

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.beta.soft.utils.ShadowSpec
import com.beta.soft.utils.dropShadow
fun Modifier.customDropShadow(
    shape: Shape = RoundedCornerShape(24.dp),
    color: Color = Color(0x400F0046),
    blur: Dp = 10.4.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp
): Modifier = this.dropShadow(
    shape = shape,
    color = color,
    blur = blur,
    offsetX = offsetX,
    offsetY = offsetY,
    spread = spread
)

fun Modifier.customDropShadowCircle(
    shape: Shape = CircleShape,
    color: Color = Color(0x400F0046),
    blur: Dp = 10.4.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp
): Modifier = this.dropShadow(
    shape = shape,
    color = color,
    blur = blur,
    offsetX = offsetX,
    offsetY = offsetY,
    spread = spread
)
fun Modifier.appShadow(spec: ShadowSpec, shape: Shape): Modifier = this.dropShadow(
    shape = shape,
    color = spec.color,
    blur = spec.blur,
    offsetX = spec.offsetX,
    offsetY = spec.offsetY,
    spread = spec.spread
)
