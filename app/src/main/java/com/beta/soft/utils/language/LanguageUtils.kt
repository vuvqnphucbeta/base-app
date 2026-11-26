package com.beta.soft.utils.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.beta.soft.models.local.data.LANG
import com.beta.soft.utils.config.LocalConfigManager

fun changeLanguage(lang: LANG) {
    val localeCode = when (lang) {
        LANG.AR -> "ar"
        LANG.CS -> "cs"
        LANG.DE -> "de"
        LANG.ES -> "es"
        LANG.FR -> "fr"
        LANG.HI -> "hi"
        LANG.IN -> "id"
        LANG.IT -> "it"
        LANG.JA -> "ja"
        LANG.KO -> "ko"
        LANG.MS -> "ms"
        LANG.PHI -> "fil"
        LANG.PL -> "pl"
        LANG.PT -> "pt-PT"
        LANG.BR -> "pt-BR"
        LANG.RU -> "ru"
        LANG.TR -> "tr"
        LANG.VI -> "vi"
        LANG.ZH -> "zh-CN"
        LANG.TW -> "zh-TW"
        LANG.TH -> "th"
        LANG.EN, LANG.EN_US -> "en"
    }

    val localeList = LocaleListCompat.forLanguageTags(localeCode)
    AppCompatDelegate.setApplicationLocales(localeList)
    LocalConfigManager.setLanguageApp(lang)
}

fun getSelectLanguageTitle(lang: LANG): String {
    return when (lang) {
        LANG.AR -> "اختر اللغة"
        LANG.CS -> "Vyberte jazyk"
        LANG.DE -> "Sprache wählen"
        LANG.ES -> "Seleccionar idioma"
        LANG.FR -> "Sélectionner la langue"
        LANG.HI -> "भाषा चुनें"
        LANG.IN -> "Pilih Bahasa"
        LANG.IT -> "Seleziona lingua"
        LANG.JA -> "言語を選択"
        LANG.KO -> "언어 선택"
        LANG.MS -> "Pilih Bahasa"
        LANG.PHI -> "Pumili ng Wika"
        LANG.PL -> "Wybierz język"
        LANG.PT -> "Selecionar idioma"
        LANG.BR -> "Selecionar idioma"
        LANG.RU -> "Выберите язык"
        LANG.TR -> "Dil Seçin"
        LANG.VI -> "Chọn ngôn ngữ"
        LANG.ZH -> "选择语言"
        LANG.TW -> "選擇語言"
        LANG.TH -> "เลือกภาษา"
        LANG.EN, LANG.EN_US -> "Select Language"
    }
}
fun getApplyButtonText(lang: LANG): String {
    return when (lang) {
        LANG.AR -> "تطبيق"
        LANG.CS -> "Použít"
        LANG.DE -> "Anwenden"
        LANG.ES -> "Aplicar"
        LANG.FR -> "Appliquer"
        LANG.HI -> "लागू करें"
        LANG.IN -> "Terapkan"
        LANG.IT -> "Applica"
        LANG.JA -> "適用する"
        LANG.KO -> "적용"
        LANG.MS -> "Gunakan"
        LANG.PHI -> "I-apply"
        LANG.PL -> "Zastosuj"
        LANG.PT -> "Aplicar"
        LANG.BR -> "Aplicar"
        LANG.RU -> "Применить"
        LANG.TR -> "Uygula"
        LANG.VI -> "Áp dụng"
        LANG.ZH -> "应用"
        LANG.TW -> "套用"
        LANG.TH -> "นำไปใช้"
        LANG.EN, LANG.EN_US -> "Apply"
    }
}