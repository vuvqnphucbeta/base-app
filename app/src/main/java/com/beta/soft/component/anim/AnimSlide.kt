package com.beta.soft.component.anim

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AnimSlide(
    index: Int,
    startAnimation: Boolean,
    modifier: Modifier = Modifier,
    slideOffsetDp: Float = 300f, // Độ lệch pixel khi bắt đầu trượt (Chỉnh độ dài quãng đường)
    content: @Composable (Modifier) -> Unit
) {
    // 1. Xác định hướng trượt dựa vào index chẵn hay lẻ
    val isEven = index % 2 == 0
    val initialTranslationX = if (isEven) -slideOffsetDp else slideOffsetDp

    // 2. Animation mờ đục dần (Fade in)
    val animatedAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = index * 100, // Stagger delay theo index
            easing = FastOutSlowInEasing
        ), label = "alpha_slide"
    )

    // 3. Animation trượt trục X (TranslationX)
    val animatedTranslationX by animateFloatAsState(
        targetValue = if (startAnimation) 0f else initialTranslationX,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = index * 100,
            easing = FastOutSlowInEasing
        ), label = "translation_x"
    )

    content(
        modifier.graphicsLayer {
            alpha = animatedAlpha
            translationX = animatedTranslationX
        }
    )
}