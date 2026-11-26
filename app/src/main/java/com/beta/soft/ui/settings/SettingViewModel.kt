package com.beta.soft.ui.settings

import androidx.lifecycle.ViewModel
import com.beta.soft.utils.nav.AppNav
import com.beta.soft.utils.nav.LanguageRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appNav: AppNav,
) : ViewModel() {

    fun navigateBack() {
        appNav.popBackStack()
    }

    fun navToSetting() {
        appNav.navigateTo(LanguageRoute)
    }
}