package com.beta.soft

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.beta.soft.component.dialog.internet.DialogCheckInternet
import com.beta.soft.component.dialog.update_app.DialogUpdateApp
import com.beta.soft.utils.nav.AppNavHost
import com.beta.soft.utils.nav.SplashRoute
import com.beta.soft.utils.system.SystemUiManager
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri
import com.beta.soft.utils.log_event.LogEventUtils

@AndroidEntryPoint
class AppMainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val appViewModel: AppMainViewModel = hiltViewModel()
            val isConnected by appViewModel.isConnected.collectAsState()
            AppNavHost(
                navController = navController,
                startRouter = SplashRoute,
            )
            if (!isConnected) {
                DialogCheckInternet(
                    onDismiss = {},
                    onGoToSetting = {
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }
                )
            }
            if (appViewModel.isShowDialogUpdate) {
                DialogUpdateApp(
                    onConfirm = {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()))
                        } catch (e: ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$packageName".toUri()))
                        }
                    }
                )
            }
        }
    }
    override fun onStop() {
        super.onStop()
        LogEventUtils.logExitApp()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUiManager.applyImmersiveNavigation(this)
        }
    }
}