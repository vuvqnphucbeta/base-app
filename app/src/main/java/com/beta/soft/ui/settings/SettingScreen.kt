package com.beta.soft.ui.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.beta.soft.AppMainViewModel
import com.beta.soft.R
import com.beta.soft.component.anim.AnimSlowMotionDisplay
import com.beta.soft.component.anim.createCustomTheme
import com.beta.soft.component.dialog.rate_app.DialogRatting
import com.beta.soft.component.topbar_common.TopBarCommon
import com.beta.soft.ui.theme.TextStyleCommon
import com.beta.soft.ui.theme.fontAmikoBold
import com.beta.soft.ui.theme.fontAmikoRegular
import com.beta.soft.ui.theme.fontMontserratBold
import com.beta.soft.ui.theme.fontMontserratSemibold
import com.beta.soft.ui.theme.font_quicksand_bold
import com.beta.soft.utils.CommonUtils.createToastDispatcher
import com.beta.soft.utils.CommonUtils.goToWebURL
import com.beta.soft.utils.CommonUtils.shareApp
import com.beta.soft.utils.CommonUtils.startEmail
import com.beta.soft.utils.config.LocalConfigManager
import com.beta.soft.utils.system.clickableOnce
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlin.compareTo

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    appMainViewModel: AppMainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }
    var isShowDialogRatting by rememberSaveable { mutableStateOf(false) }
    BackHandler {
        settingViewModel.navigateBack()
    }
    LaunchedEffect(Unit) {
        appMainViewModel.logEventShowSetting()
        startAnimation = true
    }
    Box(Modifier.fillMaxSize()) {
        GlideImage(
            model = R.drawable.bg_app,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopBarCommon(
                ImageL = R.drawable.ic_launcher_foreground,
                title = stringResource(R.string.setting),
                ImageR = if(LocalConfigManager.getShowUiIap()){ R.drawable.ic_launcher_foreground}else null,
                onLeftClick = {
                    settingViewModel.navigateBack()
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.fillMaxWidth().aspectRatio(30f))
                if(LocalConfigManager.getShowUiIap()){
                    AnimSlowMotionDisplay(index = 0, startAnimation = startAnimation) { animatedModifier ->
                        Box(modifier = animatedModifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            PremiumBox()
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
                AnimSlowMotionDisplay(index = 1, startAnimation = startAnimation) { animatedModifier ->
                    Column(modifier = animatedModifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.general),
                            style = TextStyleCommon(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W900,
                                textAlign = TextAlign.Start,
                                fontFamily = font_quicksand_bold,
                            ).copy(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xffd4e3ff),
                                        Color(0xff95baff)
                                    )
                                )
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        ItemSetting(
                            icon = R.drawable.ic_launcher_foreground,
                            txt = stringResource(R.string.language),
                            onClick = {
                                appMainViewModel.clickLanguageSetting()
                                settingViewModel.navToSetting()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                AnimSlowMotionDisplay(index = 2, startAnimation = startAnimation) { animatedModifier ->
                    Column(modifier = animatedModifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.privacy),
                            style = TextStyleCommon(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W900,
                                textAlign = TextAlign.Start,
                                fontFamily = font_quicksand_bold,
                            ).copy(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xffd4e3ff),
                                        Color(0xff95baff)
                                    )
                                )
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        ItemSetting(
                            icon = R.drawable.ic_launcher_foreground,
                            txt = stringResource(R.string.privacy_policy),
                            onClick = {
                                appMainViewModel.clickPrivacyPolicy()
                                goToWebURL(context,"https://sites.google.com/alphasoftxglobal.com/datatransfer")
                            }
                        )

                        ItemSetting(
                            icon = R.drawable.ic_launcher_foreground,
                            txt = stringResource(R.string.term_of_use),
                            onClick = {
                                appMainViewModel.clickTermOfUse()
                                goToWebURL(context,"https://sites.google.com/alphasoftxglobal.com/termsofservicedatatransfer?usp=sharing")
                            }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                AnimSlowMotionDisplay(index = 3, startAnimation = startAnimation) { animatedModifier ->
                    Column(modifier = animatedModifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.others),
                            style = TextStyleCommon(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W900,
                                textAlign = TextAlign.Start,
                                fontFamily = font_quicksand_bold,
                            ).copy(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xffd4e3ff),
                                        Color(0xff95baff)
                                    )
                                )
                            )
                        )
                        Spacer(Modifier.height(8.dp))

                        /*ItemSetting(
                            icon = R.drawable.ic_rate,
                            txt = stringResource(R.string.rate_app),
                            onClick = {
                                appMainViewModel.clickRateApp()
                                isShowDialogRatting = true
                            }
                        )*/

                        ItemSetting(
                            icon = R.drawable.ic_launcher_foreground,
                            txt = stringResource(R.string.contact_us),
                            onClick = {
                                appMainViewModel.clickContactUs()
                                startEmail(context, to = "admin@betasoftmobile.com", subject = context.getString(R.string.app_name) + " Feedback")
                            }
                        )

                        ItemSetting(
                            icon = R.drawable.ic_launcher_foreground,
                            txt = stringResource(R.string.share_app),
                            onClick = {
                                appMainViewModel.clickShareApp()
                                context.shareApp(text = context.getString(R.string.check_out_this_app))
                            }
                        )
                    }
                }

                Spacer(Modifier.height(100.dp))
            }
        }
    }
    if (isShowDialogRatting) {
        DialogRatting(
            onDismissRequest = {
                isShowDialogRatting = false
            },
            onRate = { rate ->
                isShowDialogRatting = false
                if (rate <= 4) {
                    context.createToastDispatcher("Thank you very much for your support")
                } else {
                    context.createToastDispatcher("Thank you very much for your support")
                }
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PremiumBox(
) {
    val customShimmer: Shimmer = rememberShimmer(ShimmerBounds.View, createCustomTheme())

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(2.12f)
            .clip(RoundedCornerShape(15.dp))
            .shimmer(customShimmer)
    ) {
        GlideImage(
            model = R.drawable.ic_launcher_foreground, null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    stringResource(R.string.transfer_data),
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                    color = Color(0xffff823f),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(900),
                    fontFamily = fontMontserratBold,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    stringResource(R.string.in_one_click),
                    modifier = Modifier.padding(start = 20.dp),
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontFamily = fontAmikoBold,
                    fontWeight = FontWeight(600)
                )
            }
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier.padding(start = 20.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .zIndex(1f)
                            .offset(x = (-17).dp, y = (-17).dp)
                            .align(Alignment.TopStart)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .wrapContentHeight()
                            .zIndex(2f)
                            .align(Alignment.BottomEnd)
                    ) {
                        GlideImage(
                            model = R.drawable.ic_launcher_foreground,
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.FillBounds
                        )

                        Text(
                            text = stringResource(R.string.try_now),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 17.dp, horizontal = 5.dp)
                                .basicMarquee(),
                            style = TextStyleCommon(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview()
fun SettingScreenP() {
    SettingScreen()
}

@Composable
fun ItemSetting(
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int,
    txt: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableOnce {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .padding(start = 12.dp, end = 6.dp)
                .weight(1f),
            text = txt,
            style = TextStyleCommon(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color.White,
                textAlign = TextAlign.Start
            ),
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }

}
