package com.beta.soft

import android.util.Log
import com.beta.soft.BuildConfig
import com.beta.soft.models.local.data.LANG
import com.beta.soft.utils.config.LocalConfigManager
import com.beta.soft.utils.language.changeLanguage
import com.beta.soft.utils.log_event.LogEventUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mono.beta_jsc_lib.utils.MyApplication
import com.mono.beta_jsc_lib.utils.SmartTechInit
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : MyApplication() {

    override fun initApplication() {
        super.initApplication()
        Hawk.init(this).build()
        SmartTechInit.init(this, BuildConfig.LICENSE_KEY) {
            appPackageName = packageName
            organization = ""  // Để trống: dùng chữ ký Google Play; có giá trị: verify thêm qua server
            // true: resume → loadAndShow; false: resume → showIfAvailable (cần preload)
            onReady = {
                // Tuỳ chọn: init ads sau khi Hawk + Billing sẵn sàng
                Log.d("SmartTechInit", "onReady")
            }
        }.setConfigNotificationAdsClick(isOn = true)
        checkAndSetLanguageDefault()
        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = false
            FirebaseApp.getInstance().isDataCollectionDefaultEnabled = false
        } else {
            LogEventUtils.createInstance(this@MyApp)
        }
    }

    private fun checkAndSetLanguageDefault() {
        val language = LocalConfigManager.getLanguageApp()
        Log.e("TAG", "checkAndSetLanguageDefault: $language")
        if (language == null) {
            val configuration = this@MyApp.resources.configuration

            @Suppress("DEPRECATION")
            val locale = configuration.locale
            LocalConfigManager.setLanguageApp(
                when {
                    locale.language == "zh" && locale.country.equals(
                        "CN",
                        true
                    ) -> LANG.ZH   // Trung Quốc (简体)
                    locale.language == "zh" && locale.country.equals(
                        "TW",
                        true
                    ) -> LANG.TW // Đài Loan (繁體)
                    locale.language == "pt" && locale.country.equals("BR", true) -> LANG.BR
                    locale.language == "pt" && locale.country.equals("PT", true) -> LANG.PT

                    locale.language == "ar" -> LANG.AR
                    locale.language == "tr" -> LANG.TR
                    locale.language == "vi" -> LANG.VI
                    locale.language == "th" -> LANG.TH
                    locale.language == "ru" -> LANG.RU
                    locale.language == "pl" -> LANG.PL
                    locale.language == "ko" -> LANG.KO
                    locale.language == "ja" -> LANG.JA
                    locale.language == "it" -> LANG.IT
                    locale.language == "de" -> LANG.DE
                    locale.language == "cs" -> LANG.CS
                    locale.language == "hi" -> LANG.HI
                    locale.language == "fr" -> LANG.FR
                    locale.language == "in" -> LANG.IN
                    locale.language == "ms" -> LANG.MS
                    locale.language == "phi" -> LANG.PHI
                    locale.language == "es" -> LANG.ES
                    else -> LANG.EN
                }
            )
        } else {
            changeLanguage(language)
        }
    }
}
