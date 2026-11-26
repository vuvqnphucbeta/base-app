package com.beta.soft.utils.nav

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data object SplashRoute : BaseRoute()

@Keep
@Serializable
data object LanguageRoute : BaseRoute()

@Keep
@Serializable
data object OnbroadRoute : BaseRoute()

@Keep
@Serializable
data object MainRoute : BaseRoute()

@Keep
@Serializable
data object SettingRoute : BaseRoute()
