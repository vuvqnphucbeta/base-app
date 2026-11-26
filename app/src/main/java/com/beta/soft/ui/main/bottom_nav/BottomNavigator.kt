package com.beta.soft.ui.main.bottom_nav

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.beta.soft.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BottomNavigator(
    modifier: Modifier = Modifier,
    typeSelected: BottomNav = BottomNav.HOME,
    onClick: (BottomNav) -> Unit,
) {
    val borderGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xff8b91a9),
            Color(0xff183152),
            Color(0xff183152),
            Color(0xff8b91a9),
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(5f)
            .clip(shape = RoundedCornerShape(50.dp))
            .background(color = Color(0xFF0F172A))
            .border(
                width = 1.dp,
                brush = borderGradient,
                shape = RoundedCornerShape(50.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
            ) {
                ItemBottomNav(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = R.drawable.ic_launcher_foreground,
                    title = stringResource(R.string.quick_transfer),
                    selected = typeSelected == BottomNav.HOME,
                    onClick = {
                        onClick(BottomNav.HOME)
                    }
                )

                ItemBottomNav(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = R.drawable.ic_launcher_foreground,
                    title = stringResource(R.string.phone_clone),
                    selected = typeSelected == BottomNav.PHONECLONE,
                    onClick = {
                        onClick(BottomNav.PHONECLONE)
                    }
                )
                ItemBottomNav(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = R.drawable.ic_launcher_foreground,
                    title = stringResource(R.string.my_file),
                    selected = typeSelected == BottomNav.MY_FILE,
                    onClick = {
                        onClick(BottomNav.MY_FILE)
                    }
                )
            }
        }
    }
}


@Composable
@Preview
fun BottomNavP() {
    BottomNavigator(
        typeSelected = BottomNav.HOME,
        onClick = {}
    )
}

@Keep
enum class BottomNav {
    HOME, PHONECLONE, MY_FILE
}