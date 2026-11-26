package com.beta.soft.ui.main

import androidx.lifecycle.ViewModel
import com.beta.soft.utils.log_event.LogEventUtils
import com.beta.soft.utils.log_event.MainEvent
import com.beta.soft.utils.nav.AppNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val appNav: AppNav
) : ViewModel() {
    private val pageStack = mutableListOf<Int>()

    fun selectTab(index: Int) {
        updateStack(index)
        appNav.selectTab(index)
    }

    fun updateStack(index: Int) {
        if (pageStack.isEmpty() || pageStack.last() != index) {
            if (pageStack.contains(index)) {
                pageStack.remove(index)
            }
            pageStack.add(index)
        }
    }

    private val _showExitDialog = MutableStateFlow(false)
    val showExitDialog = _showExitDialog.asStateFlow()

    fun toggleExitDialog(show: Boolean) {
        _showExitDialog.value = show
    }

    fun handleBackNavigation(currentPage: Int) {
        if (currentPage != 0) {
            if (pageStack.size > 1) {
                pageStack.removeAt(pageStack.lastIndex)
                val previousPage = pageStack.last()
                appNav.selectTab(previousPage)
            } else {
                pageStack.clear()
                pageStack.add(0)
                appNav.selectTab(0)
            }
        } else {
            toggleExitDialog(true)
        }
    }
}