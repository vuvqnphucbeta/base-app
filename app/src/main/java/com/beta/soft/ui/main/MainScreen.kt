package com.beta.soft.ui.main

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.beta.soft.AppMainViewModel
import com.beta.soft.R
import com.beta.soft.component.dialog.exit_app.DialogExitApp
import com.beta.soft.ui.main.bottom_nav.BottomNav
import com.beta.soft.ui.main.bottom_nav.BottomNavigator
import com.beta.soft.ui.main.home.HomeScreen
import com.beta.soft.ui.main.my_file.MyFileScreen
import com.beta.soft.ui.main.phoneclone.PhoneCloneScreen

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen(
    currentIndexTab: Int = 0,
    mainViewModel: MainViewModel = hiltViewModel(),
    appMainViewModel: AppMainViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = currentIndexTab) { 3 }
    val showExitDialog by mainViewModel.showExitDialog.collectAsState()
    val context = LocalContext.current
    val isMainTab = pagerState.currentPage in listOf(0, 1, 2)
    BackHandler(enabled = isMainTab) {
        mainViewModel.handleBackNavigation(pagerState.currentPage)
    }
    val selectedTab = when (pagerState.currentPage) {
        0 -> BottomNav.HOME
        1 -> BottomNav.PHONECLONE
        2 -> BottomNav.MY_FILE
        else -> BottomNav.HOME
    }
    LaunchedEffect(currentIndexTab) {
        mainViewModel.updateStack(currentIndexTab)
        pagerState.scrollToPage(currentIndexTab)
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> HomeScreen()
                1 -> PhoneCloneScreen()
                2 -> MyFileScreen()
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .background(Color.Transparent)
                .padding(bottom = 10.dp)
                .padding(horizontal = 20.dp)
        ) {
            BottomNavigator(
                modifier = Modifier.fillMaxWidth(),
                onClick = { tab ->
                    val targetPage = when (tab) {
                        BottomNav.HOME -> {
                            appMainViewModel.logEventClickHome()
                            0
                        }
                        BottomNav.PHONECLONE -> {
                            appMainViewModel.logEventClickPhoneClone()
                            1
                        }
                        BottomNav.MY_FILE -> {
                            appMainViewModel.logEventClickMyFile()
                            2
                        }
                    }
                    mainViewModel.selectTab(targetPage)
                },
                typeSelected = selectedTab,
            )
        }
    }
    if (showExitDialog) {
        DialogExitApp(
            onDismiss = { mainViewModel.toggleExitDialog(false) },
            onConfirm = {
                (context as? ComponentActivity)?.finish()
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@Preview(showSystemUi = true)
fun MainScreenP() {
    MainScreen(
        currentIndexTab = 0,
    )
}