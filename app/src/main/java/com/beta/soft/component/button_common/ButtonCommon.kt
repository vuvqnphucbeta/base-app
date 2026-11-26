package com.beta.soft.component.button_common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beta.soft.utils.system.clickableOnce

@Composable
fun ButtonCommon(
    modifier: Modifier = Modifier,
    text: String = "",
    width: Dp? = null,
    height: Dp = 56.dp,
    cornerRadius: Dp = 28.dp,
    fontSize: Int = 18,
    textColor: Color = Color.White,
    backgroundBrush: Brush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Transparent)
    ),
    borderWidth: Dp? = null,
    borderColor: Color = Color.Transparent,
    onClick: () -> Unit = {}
) {
    val buttonModifier = modifier
        .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
        .height(height)
        .clip(RoundedCornerShape(cornerRadius))
        .background(brush = backgroundBrush)
        .then(
            if (borderWidth != null) Modifier.border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
            else Modifier
        )
        .clickableOnce { onClick() }

    Box(
        modifier = buttonModifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}