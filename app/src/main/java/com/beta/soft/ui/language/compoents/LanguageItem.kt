package com.beta.soft.ui.language.compoents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beta.soft.R
import com.beta.soft.component.anim.customDropShadow
import com.beta.soft.models.local.data.Language
import com.beta.soft.ui.theme.TextStyleCommon
import com.beta.soft.ui.theme.fontAmikoBold
import com.beta.soft.ui.theme.fontAmikoRegular
import com.beta.soft.utils.system.clickableOnce

@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onLanguageSelected: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .customDropShadow(
                shape = RoundedCornerShape(36.dp),
                color = Color(0x80000000).copy(alpha = 0.2f),
                blur = 20.dp,
                offsetX = 0.dp,
                offsetY = 8.dp,
                spread = 2.dp
            )
            .clip(RoundedCornerShape(40.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                id = if (isSelected) R.drawable.ic_launcher_foreground
                else R.drawable.ic_launcher_foreground
            ),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clickableOnce { onLanguageSelected() },
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = language.icon,
                    fontSize = 30.sp,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = language.name,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = fontAmikoBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = language.code.name,
                    style = TextStyleCommon(
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontFamily = fontAmikoRegular
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}