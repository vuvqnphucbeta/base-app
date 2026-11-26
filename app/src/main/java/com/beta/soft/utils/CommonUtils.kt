package com.beta.soft.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.beta.soft.R
import com.google.android.play.core.review.ReviewManagerFactory
import com.mono.beta_jsc_lib.utils.extension.findActivity

object CommonUtils {
    fun Context.shareApp(text: String) {
        val appPackageName = packageName
        val playUrl = "https://play.google.com/store/apps/details?id=$appPackageName"
        val message = """
        $text
        $playUrl
    """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "PhotoPose - Try these creative poses!")
            putExtra(Intent.EXTRA_TEXT, message)
        }

        val chooser = Intent.createChooser(intent, "Share PhotoPose")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }

    fun goToWebURL(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = url.toUri()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (_: Exception) {
        }

    }
    fun startEmail(context: Context, to: String, subject: String, content: String? = null) {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, content ?: context.getString(R.string.feedback_content))
        intent.selector = selectorIntent
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                context.startActivity(Intent.createChooser(intent, "Send email..."))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun Context.createToastDispatcher(text: String, isLong: Boolean = false) {
        findActivity().runOnUiThread { createToast(text, isLong) }
    }
    fun Context.createToast(text: String, isLong: Boolean = false) {
        Toast.makeText(this, text, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
    fun Context.openPlayStore() {
        val appPackageName = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$appPackageName".toUri()))
        } catch (e: Exception) {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$appPackageName".toUri()))
            } catch (_: ActivityNotFoundException) {
            }
        }
    }
    fun launchInAppReview(
        activity: Activity,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        if (activity.isFinishing || activity.isDestroyed) return
        try {
            val reviewManager = ReviewManagerFactory.create(activity.applicationContext)
            val requestReviewFlow = reviewManager.requestReviewFlow()
            requestReviewFlow.addOnCompleteListener { request ->
                if (activity.isFinishing || activity.isDestroyed) return@addOnCompleteListener
                if (request.isSuccessful) {
                    val reviewInfo = request.result
                    reviewManager.launchReviewFlow(activity, reviewInfo)
                        .addOnCompleteListener { flowTask ->
                            if (flowTask.isSuccessful) {
                                onSuccess()
                            } else {
                                onFailure()
                                activity.openPlayStore()
                            }
                        }
                } else {
                    onFailure()
                    activity.openPlayStore()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure()
            if (!activity.isFinishing && !activity.isDestroyed) {
                activity.openPlayStore()
            }
        }
    }
}
