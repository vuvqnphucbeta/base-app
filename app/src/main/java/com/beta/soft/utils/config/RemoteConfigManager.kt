package com.beta.soft.utils.config

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.beta.soft.AppMainViewModel
import com.beta.soft.utils.ForceUpdateConfig
import com.beta.soft.utils.config.Constants.KEY_FORCE_UPDATE
import com.beta.soft.utils.config.Constants.KEY_SHOW_UI_IAP
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.mono.beta_jsc_lib.utils.LogTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object RemoteConfigManager {
    @SuppressLint("StaticFieldLeak")
    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private var isLoadedSuccessfully = false

    init {
        val builder = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .setFetchTimeoutInSeconds(15)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(builder)
    }

    suspend fun startFirstOpenRemoteConfig(
        appViewModel: AppMainViewModel,
        context: Context,
        intervalRetryMs: Long = 15000
    ) {
        val success = loadOnce(
            context = context,
            appViewModel = appViewModel
        )
        Log.d(this.javaClass.simpleName, "startFirstOpenRemoteConfig Success: $success")
        if (success) {
            isLoadedSuccessfully = true
        } else {
            startAutoLoad(
                context = context,
                intervalRetryMs = intervalRetryMs,
                appViewModel = appViewModel
            )
        }
    }

    fun startAutoLoad(
        context: Context,
        intervalRetryMs: Long = 15000,
        appViewModel: AppMainViewModel
    ) {
        if (isLoadedSuccessfully) return
        CoroutineScope(Dispatchers.IO).launch {
            while (!isLoadedSuccessfully) {
                val success = loadOnce(
                    context = context,
                    appViewModel = appViewModel
                )
                Log.d(
                    this@RemoteConfigManager.javaClass.simpleName,
                    "startAutoLoad: Success: $success"
                )
                if (success) {
                    isLoadedSuccessfully = true
                } else {
                    delay(timeMillis = intervalRetryMs)
                }
            }
        }

    }

    suspend fun loadOnce(
        context: Context,
        appViewModel: AppMainViewModel
    ): Boolean = withContext(Dispatchers.IO) {
        LogTimeUtils.markTime("Remote Config")
        val isSuccessful = try {
            val task = firebaseRemoteConfig.fetchAndActivate()
            task.await()
            task.isSuccessful
        } catch (e: Exception) {
            Log.e(this@RemoteConfigManager.javaClass.simpleName, "loadOnce: Fetch failed", e)
            return@withContext false
        }

        Log.d(
            this@RemoteConfigManager.javaClass.simpleName,
            "loadOnce: isSuccessful= $isSuccessful"
        )

        if (isSuccessful) {
            Log.d("RemoteConfig", "✅ Connected to Firebase Remote Config")
            saveAllConfigs(
                context = context,
                config = firebaseRemoteConfig,
                appViewModel = appViewModel
            )
            LogTimeUtils.logTime("Remote Config")
            true
        } else {
            Log.e("RemoteConfig", "❌ Failed to connect Firebase Remote Config")
            false
        }
    }

    private suspend fun saveAllConfigs(
        context: Context,
        config: FirebaseRemoteConfig,
        appViewModel: AppMainViewModel
    ) = withContext(Dispatchers.IO) {
        val forceUpdateJson = config.getString(KEY_FORCE_UPDATE)
        val forceUpdateConfig = try {
            if (forceUpdateJson.isBlank()) {
                ForceUpdateConfig()
            } else {
                Gson().fromJson(forceUpdateJson, ForceUpdateConfig::class.java)
            }
        } catch (e: Exception) {
            ForceUpdateConfig()
        }
        LocalConfigManager.setForceUpdateApp(forceUpdateConfig)
        LocalConfigManager.setShowUiIap(config.getBoolean(KEY_SHOW_UI_IAP))

        appViewModel.checkForceUpdate()
    }
}