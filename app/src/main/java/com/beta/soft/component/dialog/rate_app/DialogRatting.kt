package com.beta.soft.component.dialog.rate_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.beta.soft.R
import com.beta.soft.ui.theme.TextStyleCommon
import com.beta.soft.ui.theme.fontMontserratRegular
import com.beta.soft.utils.CommonUtils.launchInAppReview
import com.beta.soft.utils.rate.RatingBar
import com.beta.soft.utils.system.SystemUiManager
import com.beta.soft.utils.system.clickableOnce
import com.mono.beta_jsc_lib.utils.extension.findActivity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DialogRatting(
    onDismissRequest: () -> Unit,
    onRate: (Int) -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect {
            dialogWindow?.let { window ->
                window.setDimAmount(0.6f)
                SystemUiManager.hideSystemBars(window)
            }
        }

        val density = LocalDensity.current
        val context = LocalContext.current
        var rating by remember { mutableFloatStateOf(5f) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .background(Color.Black)
                    .clip(RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = stringResource(R.string.rate_our_app),
                        style = TextStyleCommon(
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFFF9800), // Cam ấm (thay thế Warning2)
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var outlineButtonRect by remember { mutableStateOf(Rect.Zero) }

                    // Rating Star Bar
                    RatingBar(
                        value = rating,
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                outlineButtonRect = coordinates.boundsInParent()
                            },
                        size = 38.dp,
                        spaceBetween = 10.dp,
                        painterEmpty = painterResource(id = R.drawable.ic_launcher_background),
                        painterFilled = painterResource(id = R.drawable.ic_launcher_foreground),
                        onValueChange = { rating = it },
                        onRatingChanged = { rating = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle kèm icon chỉ hướng
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = stringResource(R.string.please_rate_5_stars),
                            style = TextStyleCommon(
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = fontMontserratRegular,
                                color = Color(0xFF212121), // Xám đen (thay thế Neutral900)
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )

                        if (outlineButtonRect.width > 0f) {
                            val offsetX = with(density) { (outlineButtonRect.right - 24.dp.toPx()).toDp() }
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .offset(x = offsetX, y = (-2).dp)
                                    .size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Rate Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF1DBBFF), Color(0xFF0048FF))
                                )
                            )
                            .clickableOnce {
                                if (rating > 4) {
                                    launchInAppReview(
                                        activity = context.findActivity(),
                                        onSuccess = { onRate(rating.toInt()) },
                                        onFailure = { onRate(rating.toInt()) }
                                    )
                                } else {
                                    onRate(rating.toInt())
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.submit),
                            style = TextStyleCommon(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                // Nút Đóng (TopEnd)
                GlideImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickableOnce {
                            onDismissRequest()
                        }
                )
            }
        }
    }
}