package com.beta.soft.ui.language

import androidx.lifecycle.ViewModel
import com.beta.soft.models.local.data.LANG
import com.beta.soft.models.local.data.Language
import com.beta.soft.utils.language.Constants
import com.beta.soft.utils.config.LocalConfigManager
import com.beta.soft.utils.language.changeLanguage
import com.beta.soft.utils.log_event.LanguageEvent
import com.beta.soft.utils.log_event.LogEventUtils
import com.beta.soft.utils.nav.AppNav
import com.beta.soft.utils.nav.MainRoute
import com.beta.soft.utils.nav.OnbroadRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val appNav: AppNav,
) : ViewModel() {

    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages: StateFlow<List<Language>> = _languages.asStateFlow()

    private val _selectedLanguage = MutableStateFlow<LANG>(LANG.EN)
    val selectedLanguage: StateFlow<LANG> = _selectedLanguage.asStateFlow()

    init {
        loadLanguages()
    }

    private fun loadLanguages() {
        val currentLang = LocalConfigManager.getLanguageApp() ?: LANG.EN
        _selectedLanguage.value = currentLang

        val allLanguages = Constants.listLanguageApp.map { lang ->
            lang.copy(selected = lang.code == currentLang)
        }

        _languages.value = allLanguages
    }

    fun selectLanguage(language: Language) {
        _selectedLanguage.value = language.code

        // Update the list with new selection
        _languages.value = _languages.value.map { lang ->
            lang.copy(selected = lang.code == language.code)
        }
    }

    fun onDoneClick() {
        changeLanguage(_selectedLanguage.value)
        val firstOpen = LocalConfigManager.getFirstOpen()
        /*if (firstOpen){
            appNav.navigateTo(OnbroadRoute)
        }else{
            appNav.navigateTo(MainRoute)
        }*/

        appNav.navigateTo(MainRoute)
    }

    fun onBack() {
        appNav.popBackStack()
    }
}