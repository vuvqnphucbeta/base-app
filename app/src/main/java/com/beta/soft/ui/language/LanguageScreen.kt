package com.beta.soft.ui.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.beta.soft.AppMainViewModel
import com.beta.soft.R
import com.beta.soft.component.anim.animateSlideInBottom
import com.beta.soft.component.anim.createCustomTheme
import com.beta.soft.component.topbar_common.TopBarCommon
import com.beta.soft.ui.language.compoents.LanguageItem
import com.beta.soft.ui.theme.TextStyleCommon
import com.beta.soft.ui.theme.fontMontserratBold
import com.beta.soft.utils.config.LocalConfigManager
import com.beta.soft.utils.language.getApplyButtonText
import com.beta.soft.utils.language.getSelectLanguageTitle
import com.beta.soft.utils.system.clickableOnce
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LanguageScreen(
    languageViewModel: LanguageViewModel = hiltViewModel(),
    appMainViewModel: AppMainViewModel = hiltViewModel()
) {
    val languages by languageViewModel.languages.collectAsState()
    val selectedLanguage by languageViewModel.selectedLanguage.collectAsState()
    val firstOpen = LocalConfigManager.getFirstOpen()
    val customShimmer: Shimmer = rememberShimmer(ShimmerBounds.View, createCustomTheme())
    LaunchedEffect(Unit) {
        if (firstOpen){
            appMainViewModel.logEventLanguageFirst()
        }else{
            appMainViewModel.logEventLanguage()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GlideImage(
            model = R.drawable.bg_app,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            TopBarCommon(
                ImageL = if (!firstOpen) R.drawable.ic_launcher_foreground else null,
                title = getSelectLanguageTitle(selectedLanguage),
                ImageR =if(LocalConfigManager.getShowUiIap()){ R.drawable.ic_launcher_foreground}else null,
                onLeftClick = {
                    languageViewModel.onBack()
                }
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(languages, key = { it.code.name }) { language ->
                    LanguageItem(
                        language = language,
                        isSelected = language.selected,
                        onLanguageSelected = { languageViewModel.selectLanguage(language) }
                    )
                }
                item {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2.54f))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateSlideInBottom()
                .aspectRatio(2.54f)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            val hasSelection = languages.any { it.selected }
            if (hasSelection) {
                GlideImage(
                    model = R.drawable.ic_launcher_background, null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(5f)
                        .clip(RoundedCornerShape(60.dp))
                        .shimmer(customShimmer)
                        .clickableOnce { languageViewModel.onDoneClick() },
                    contentAlignment = Alignment.Center
                ) {
                    GlideImage(
                        model = com.mono.beta_jsc_lib.R.drawable.ic_down,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                        loading = placeholder(R.drawable.ic_launcher_foreground)
                    )
                    Text(
                        getApplyButtonText(selectedLanguage),
                        modifier = Modifier.padding(vertical = 18.dp, horizontal = 50.dp),
                        style = TextStyleCommon(
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily = fontMontserratBold,
                            fontWeight = FontWeight.W700
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LanguageScreenP() {
    LanguageScreen()
}