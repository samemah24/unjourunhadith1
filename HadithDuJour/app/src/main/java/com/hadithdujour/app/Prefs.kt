package com.hadithdujour.app

import android.content.Context
import java.util.Locale

class Prefs(context: Context) {

    private val sp = context.applicationContext
        .getSharedPreferences("hadith_prefs", Context.MODE_PRIVATE)

    /** "auto" ou un code de langue (fr, en, de, es). */
    var language: String
        get() = sp.getString(KEY_LANGUAGE, LANGUAGE_AUTO) ?: LANGUAGE_AUTO
        set(value) {
            sp.edit().putString(KEY_LANGUAGE, value).apply()
        }

    var notificationsEnabled: Boolean
        get() = sp.getBoolean(KEY_NOTIFICATIONS, false)
        set(value) {
            sp.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()
        }

    var notifHour: Int
        get() = sp.getInt(KEY_HOUR, 8)
        set(value) {
            sp.edit().putInt(KEY_HOUR, value).apply()
        }

    var notifMinute: Int
        get() = sp.getInt(KEY_MINUTE, 0)
        set(value) {
            sp.edit().putInt(KEY_MINUTE, value).apply()
        }

    var favorites: Set<Int>
        get() = (sp.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        set(value) {
            sp.edit().putStringSet(KEY_FAVORITES, value.map { it.toString() }.toSet()).apply()
        }

    fun effectiveLanguage(): String = resolveLanguage(language)

    companion object {
        const val LANGUAGE_AUTO = "auto"

        private const val KEY_LANGUAGE = "language"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_HOUR = "notif_hour"
        private const val KEY_MINUTE = "notif_minute"
        private const val KEY_FAVORITES = "favorites"

        /** Transforme le réglage ("auto" ou code) en langue réellement utilisée. */
        fun resolveLanguage(setting: String): String {
            if (setting != LANGUAGE_AUTO) return setting
            val system = Locale.getDefault().language
            return if (system in HadithRepository.supportedLanguages) system else "en"
        }
    }
}
