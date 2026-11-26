package com.beta.soft.utils.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.beta.soft.AppMainViewModel
import com.beta.soft.ui.language.LanguageScreen
import com.beta.soft.ui.main.MainScreen
import com.beta.soft.ui.onbroad.OnbroadScreen
import com.beta.soft.ui.settings.SettingScreen
import com.beta.soft.ui.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavHost(
    navController: NavHostController,
    startRouter: BaseRoute,
) {
    val appMainViewModel: AppMainViewModel = hiltViewModel()
    var selectedIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        appMainViewModel.appNav.navigationEvents.collect { intent ->
            when (intent) {
                is NavIntent.To -> {
                    navController.navigate(intent.route) {
                        if (intent.clearBackStack) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                }

                is NavIntent.Back -> {
                    val currentRoute = navController.currentDestination?.route
                    if (currentRoute != null && !currentRoute.contains("MainRoute")) {
                        navController.popBackStack()
                    }
                }

                is NavIntent.PopUpTo -> {
                    navController.navigate(intent.destination) {
                        popUpTo(intent.route) {
                            inclusive = intent.inclusive
                        }
                    }
                }

                is NavIntent.Tab -> {
                    selectedIndex = intent.index
                }
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = startRouter
    ) {
        composable<SplashRoute> {
            SplashScreen()
        }
        composable<LanguageRoute> {
            LanguageScreen()
        }
        composable<OnbroadRoute> {
            OnbroadScreen()
        }
        composable<MainRoute> {
            MainScreen(
                currentIndexTab = selectedIndex,
            )
        }
        composable<SettingRoute> {
            SettingScreen()
        }
    }
}