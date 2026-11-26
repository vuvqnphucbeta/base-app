package com.beta.soft.ui.main.my_file

import androidx.lifecycle.ViewModel
import com.beta.soft.utils.nav.AppNav
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyFileViewModel @Inject constructor(
    private val appNav: AppNav,
) : ViewModel() {

}