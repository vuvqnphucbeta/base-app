package com.beta.soft.ui.onbroad

import androidx.lifecycle.ViewModel
import com.beta.soft.utils.config.LocalConfigManager
import com.beta.soft.utils.log_event.LogEventUtils
import com.beta.soft.utils.log_event.OnboardEvent
import com.beta.soft.utils.nav.AppNav
import com.beta.soft.utils.nav.MainRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnbroadViewModel @Inject constructor(
    private val appNav: AppNav,
) : ViewModel() {
    fun popBackStack() {
        appNav.popBackStack()
    }

    fun finishOnboarding() {
        LocalConfigManager.setFirstOpen(false)
        appNav.navigateTo(MainRoute)
    }
}