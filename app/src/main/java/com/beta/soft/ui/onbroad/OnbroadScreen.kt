package com.beta.soft.ui.onbroad

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.beta.soft.AppMainViewModel
import com.beta.soft.component.button_common.ButtonCommon
import com.beta.soft.ui.theme.TextStyleCommon
import com.beta.soft.R
import kotlin.math.absoluteValue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OnbroadScreen(
    onbroadViewModel: OnbroadViewModel = hiltViewModel(),
    appMainViewModel: AppMainViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val listImages = remember {
        listOf(
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground
        )
    }
    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> appMainViewModel.logOnboard1()
            1 -> appMainViewModel.logOnboard2()
            2 -> appMainViewModel.logOnboard3()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Background
        GlideImage(
            model = R.drawable.bg_app,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { page -> "intro_page_$page" }
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scaleFactor = 0.92f + (1f - 0.92f) * (1f - pageOffset.absoluteValue)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                GlideImage(
                    model = listImages[page],
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.642f)
                )
            }
        }

        when (pagerState.currentPage) {
            0 -> IntroPageContent(
                titleRes = R.string.phone_clone,
                descRes = R.string.file_access_permissions,
                onNext = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )

            1 -> IntroPageContent(
                titleRes = R.string.my_file,
                descRes = R.string.videos_transfer,
                onNext = {
                    scope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                }
            )

            2 -> IntroPageContent(
                titleRes = R.string.videos_transfer,
                descRes = R.string.transfer_history,
                onNext = {
                    onbroadViewModel.finishOnboarding()
                },
                isLastPage = true
            )
        }
    }

    BackHandler {
        onbroadViewModel.popBackStack()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun IntroPageContent(
    titleRes: Int,
    descRes: Int,
    onNext: () -> Unit,
    isLastPage: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(titleRes).uppercase(),
            style = TextStyleCommon()
                .copy(
                    fontWeight = FontWeight.Black,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xffFFCD1D), Color(0xffF7589C))
                    )
                ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(descRes),
            style = TextStyleCommon()
                .copy(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xffF7589C), Color(0xffFFCD1D))
                    )
                ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Spacer(Modifier.height(32.dp))

        ButtonCommon(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .padding(horizontal = 50.dp),
            text =if (isLastPage) "get started" else "next",
            height = 64.dp,
            cornerRadius = 64.dp,
            fontSize = 14,
            backgroundBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xffFF5900),
                    Color(0xffFFDDCA)
                )
            ),
            onClick = onNext
        )
    }
}