package com.beta.soft.utils

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ShadowSpec(
    val blur: Dp,
    val offsetY: Dp,
    val spread: Dp = 0.dp,
    val offsetX: Dp = 0.dp,
    val color: Color = Color(0x400F0046),
)

@Immutable
data class AppElevation(
    val none: ShadowSpec = ShadowSpec(blur = 0.dp, offsetY = 0.dp, color = Color.Transparent),
    val subtle: ShadowSpec = ShadowSpec(blur = 4.dp, offsetY = 2.dp),
    val card: ShadowSpec = ShadowSpec(blur = 8.dp, offsetY = 6.dp, spread = (-1).dp),
    val raised: ShadowSpec = ShadowSpec(blur = 12.dp, offsetY = 6.dp, spread = (-1).dp),
    val overlay: ShadowSpec = ShadowSpec(blur = 28.dp, offsetY = 8.dp),
)

val LocalAppElevation = staticCompositionLocalOf { AppElevation() }
