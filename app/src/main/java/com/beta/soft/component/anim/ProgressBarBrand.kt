package com.beta.soft.component.anim

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beta.soft.ui.theme.TextStyleCommon

@Composable
fun ProgressBarBrand(
    modifier: Modifier = Modifier,
    progress: Float,
    onFinished: () -> Unit = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = "progress"
    )

    LaunchedEffect(animatedProgress) {
        if (animatedProgress >= 1f) {
            onFinished()
        }
    }

    val gradient =
        Brush.horizontalGradient(
            listOf(
                Color(0xffa533e0),
                Color(0xffbe68be),
                Color(0xffd5999d),
                Color(0xfff0d277)
            )
        )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xff0d0259))
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(gradient, RoundedCornerShape(32.dp))
        )

        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyleCommon(
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                color = Color.White
            )
        )
    }
}