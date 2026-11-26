package com.beta.soft.ui.main.home

import androidx.lifecycle.ViewModel
import com.beta.soft.utils.nav.AppNav
import com.beta.soft.utils.nav.SettingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appNav: AppNav
) : ViewModel() {

}