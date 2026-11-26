package com.beta.soft.ui.splash

import androidx.lifecycle.ViewModel
import com.beta.soft.utils.config.LocalConfigManager
import com.beta.soft.utils.config.RemoteConfigManager
import com.beta.soft.utils.log_event.LogEventUtils
import com.beta.soft.utils.log_event.SplashEvent
import com.beta.soft.utils.nav.AppNav
import com.beta.soft.utils.nav.LanguageRoute
import com.beta.soft.utils.nav.MainRoute
import com.beta.soft.utils.nav.SplashRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appNav: AppNav
) : ViewModel() {
    fun navToLanguage() {
        val firstOpen = LocalConfigManager.getFirstOpen()
        /** first open app :
         * if (firstOpen) {
            appNav.navigateAndPopUpTo(
                destination = LanguageRoute,
                popUpTo = SplashRoute,
                inclusive = true
            )
        } else {
            appNav.navigateAndPopUpTo(
                destination = MainRoute,
                popUpTo = SplashRoute,
                inclusive = true
            )
        }*/
        appNav.navigateAndPopUpTo(
            destination = MainRoute,
            popUpTo = SplashRoute,
            inclusive = true
        )
    }
}