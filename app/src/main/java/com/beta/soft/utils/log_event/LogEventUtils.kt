package com.beta.soft.utils.log_event

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogEventUtils {
    companion object {
        private lateinit var firebaseAnalytics: FirebaseAnalytics
        private val TAG = "LogEventUtils"
        fun createInstance(context: Context) {
            if (Companion::firebaseAnalytics.isInitialized) {
                return
            }
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        }
        var currentScreenName: String = "unknown"
            private set
        fun logScreenView(screenName: String, screenClass: String = "MainActivity") {
            currentScreenName = screenName
            CoroutineScope(Dispatchers.IO).launch {
                if (::firebaseAnalytics.isInitialized) {
                    val bundle = Bundle().apply {
                        putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                        putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
                    }
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
                }
                Log.d(TAG, "ScreenView: $screenName, Class: $screenClass")
            }
        }
        fun logExitApp(
            exitAppEvent: ExitAppEvent = ExitAppEvent.exit_app,
            lastScreen: String = currentScreenName
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                val bundle = Bundle().apply {
                    putString("lastScreen", lastScreen.trim())
                }
                Log.d(TAG, "Logged Event: ${exitAppEvent.eventName}, Data: Bundle[{lastScreen=$lastScreen}]")
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(exitAppEvent.eventName, bundle)
                }
            }
        }
        fun trackSplashEvent(event: SplashEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackSplashEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackSplashEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackLanguageEvent(event: LanguageEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackLanguageEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackLanguageEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackOnboardEvent(event: OnboardEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackOnboardEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackOnboardEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackMainEvent(event: MainEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackMainEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackMainEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackHomeEvent(event: HomeEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackHomeEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackHomeEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }

        fun trackPhotoTransferEvent(event: PhotoTransferEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackPhotoTransferEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackPhotoTransferEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackAudioTransferEvent(event: AudioTransferEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackAudioTransferEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackAudioTransferEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackVideoTransferEvent(event: VideoTransferEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackVideoTransferEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackVideoTransferEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackFilesTransferEvent(event: FilesTransferEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackFilesTransferEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackFilesTransferEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackPhoneCloneEvent(event: PhoneCloneEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackPhoneCloneEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackPhoneCloneEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackMyFileEvent(event: MyFileEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackMyFileEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackMyFileEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
        fun trackSettingEvent(event: SettingEvent, bundle: Bundle? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                val finalBundle = bundle ?: Bundle()
                if (bundle != null) {
                    Log.e(TAG, "trackSettingEvent: ${event.value} ====> $finalBundle")
                } else {
                    Log.e(TAG, "trackSettingEvent: ${event.value}")
                }
                if (event.value.startsWith("show")) {
                    logScreenView(event.value)
                }
                if (Companion::firebaseAnalytics.isInitialized) {
                    firebaseAnalytics.logEvent(event.value, finalBundle)
                }
            }
        }
    }
}


