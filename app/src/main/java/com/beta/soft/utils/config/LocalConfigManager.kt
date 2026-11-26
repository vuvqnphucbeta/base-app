package com.beta.soft.utils.config

import com.beta.soft.models.local.data.LANG
import com.beta.soft.utils.ForceUpdateConfig
import com.beta.soft.utils.config.Constants.KEY_FIRST_OPEN
import com.beta.soft.utils.config.Constants.KEY_FORCE_UPDATE
import com.beta.soft.utils.config.Constants.KEY_LANGUAGE
import com.beta.soft.utils.config.Constants.KEY_SHOW_UI_IAP
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk

object LocalConfigManager {

    fun setLanguageApp(lang: LANG) {
        Hawk.put(KEY_LANGUAGE, lang)
    }

    fun getLanguageApp(): LANG? {
        return Hawk.get(KEY_LANGUAGE, null)
    }


    fun setFirstOpen(isFirstOpen: Boolean) {
        Hawk.put(KEY_FIRST_OPEN, isFirstOpen)
    }

    fun getFirstOpen(): Boolean {
        return Hawk.get(KEY_FIRST_OPEN, true)
    }
    fun setForceUpdateApp(config: ForceUpdateConfig) {
        Hawk.put(KEY_FORCE_UPDATE, Gson().toJson(config))
    }

    fun getForceUpdateApp(): ForceUpdateConfig {
        val json = Hawk.get(KEY_FORCE_UPDATE, "")
        return try {
            if (json.isNullOrEmpty()) {
                ForceUpdateConfig()
            } else {
                Gson().fromJson(json, ForceUpdateConfig::class.java)
            }
        } catch (e: Exception) {
            ForceUpdateConfig()
        }
    }
    fun setShowUiIap(show: Boolean) {
        Hawk.put(KEY_SHOW_UI_IAP, show)
    }

    fun getShowUiIap(): Boolean {
        return Hawk.get(KEY_SHOW_UI_IAP, false)
    }
}