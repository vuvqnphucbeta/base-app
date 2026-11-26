package com.beta.soft.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.beta.soft.AppMainViewModel
import com.beta.soft.R
import com.beta.soft.ui.theme.TextStyleCommon
import com.beta.soft.ui.theme.fontAmikoBold
import com.beta.soft.utils.config.RemoteConfigManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    appMainViewModel: AppMainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        appMainViewModel.logEventShowSplashScreen()
        val taskRemoteConfig = async(Dispatchers.IO) {
            RemoteConfigManager.startFirstOpenRemoteConfig(
                appViewModel = appMainViewModel,
                context = context,
                intervalRetryMs = 5000
            )
        }
        delay(3000.milliseconds)
        try {
            awaitAll(taskRemoteConfig)
        } catch (e: Exception) {

        } finally {
            splashViewModel.navToLanguage()
        }
        return@LaunchedEffect
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            model = R.drawable.bg_app,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = R.mipmap.ic_launcher,
                null,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                stringResource(R.string.app_name),
                style = TextStyleCommon(
                    fontFamily = fontAmikoBold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    color = Color.White
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashScreenP() {
    SplashScreen()
}
