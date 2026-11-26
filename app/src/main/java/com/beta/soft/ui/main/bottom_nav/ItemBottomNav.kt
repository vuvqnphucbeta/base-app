package com.beta.soft.ui.main.bottom_nav

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beta.soft.utils.system.clickableOnce

@Composable
fun ItemBottomNav(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val selectionProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "selectionProgress"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color(0xFF9E9E9E),
        animationSpec = tween(durationMillis = 400),
        label = "textColor"
    )

    Box(
        modifier = modifier
            .clickableOnce { onClick() }
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        if (selectionProgress > 0f) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = selectionProgress * 0.8f
                    }
                    .blur(15.dp)
                    .drawBehind {
                        val width = size.width
                        val height = size.height
                        val centerX = width / 2

                        val beamPath = Path().apply {
                            moveTo(centerX - 15.dp.toPx(), height)
                            lineTo(0f, 0f)
                            lineTo(width, 0f)
                            lineTo(centerX + 15.dp.toPx(), height)
                            close()
                        }

                        drawPath(
                            path = beamPath,
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(255, 119, 44).copy(alpha = 0.5f),
                                    Color(108, 127, 255).copy(alpha = 0.2f),
                                    Color.Transparent
                                ),
                                center = Offset(centerX, height),
                                radius = width * 0.8f
                            )
                        )
                    }
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.graphicsLayer {
                val scale = 1f + (0.1f * selectionProgress)
                scaleX = scale
                scaleY = scale
                translationY = -3.dp.toPx() * selectionProgress
            }
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = title,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(42.dp * selectionProgress)
                .height(5.dp)
                .background(
                    color = Color(0xFFFF772C),
                    shape = CircleShape
                )
                .graphicsLayer {
                    alpha = selectionProgress
                }
        )
    }
}