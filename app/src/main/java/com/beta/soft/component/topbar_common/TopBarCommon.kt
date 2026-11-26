package com.beta.soft.component.topbar_common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beta.soft.ui.theme.TextStyleCommon
import com.beta.soft.ui.theme.fontAmikoBold
import com.beta.soft.utils.system.clickableOnce

@Composable
fun TopBarCommon(
    title: String,
    @DrawableRes ImageL: Int? = null,
    @DrawableRes ImageR: Int? = null,
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
    titleStyle: TextStyle = TextStyleCommon(
        fontFamily = fontAmikoBold,
        fontWeight = FontWeight(700),
        fontSize = 16.sp,
        color = Color.White,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    logoSize: Int = 40,
) {
    Box(
        modifier = modifier

            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .statusBarsPadding()
    ) {
        ImageL?.let { resId ->
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickableOnce { onLeftClick() }
            )
        }
        Text(
            text = title,
            style = titleStyle,
            modifier = Modifier
                .align(if (ImageL != null) Alignment.Center else Alignment.CenterStart)
                .padding(horizontal = if (ImageL != null) 48.dp else 0.dp)
                .basicMarquee(),
            maxLines = 1
        )
        ImageR?.let { resId ->
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(logoSize.dp)
                    .clip(CircleShape)
                    .clickableOnce { onRightClick() },
                contentScale = ContentScale.Fit
            )
        }
    }
}