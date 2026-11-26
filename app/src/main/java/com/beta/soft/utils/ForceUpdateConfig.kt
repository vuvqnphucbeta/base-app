package com.beta.soft.utils

data class ForceUpdateConfig(
    val enable: Boolean = false,
    val versionName: String = "1.0.0"
) {
    fun shouldShowDialog(currentVersionName: String): Boolean {
        if (!enable) return false
        return compareVersions(currentVersionName, versionName) < 0
    }

    companion object {
        fun compareVersions(a: String, b: String): Int {
            val partsA = a.trim().split(".").map { it.toIntOrNull() ?: 0 }
            val partsB = b.trim().split(".").map { it.toIntOrNull() ?: 0 }
            val maxLen = maxOf(partsA.size, partsB.size)
            for (i in 0 until maxLen) {
                val diff = (partsA.getOrElse(i) { 0 }) - (partsB.getOrElse(i) { 0 })
                if (diff != 0) return diff
            }
            return 0
        }
    }
}
