package com.pandacorp.knowui.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager
import com.pandacorp.knowui.R

class CustomSharedPreferences(private val context: Context) {
    companion object {
        const val THEME_FOLLOW_SYSTEM = "follow_system"
        const val THEME_BLUE = "blue"
        const val THEME_DARK = "dark"
        const val THEME_RED = "red"
        const val THEME_PURPLE = "purple"
        const val THEME_DEFAULT = THEME_FOLLOW_SYSTEM

        const val THEME_KEY = "theme"
        const val LANGUAGE_KEY = "language"

    }

    private var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    fun getThemesKeys(): List<String> =
        context.resources.getStringArray(R.array.Themes_keys).toList()

    fun getLanguagesKeys(): List<String> =
        context.resources.getStringArray(R.array.Languages_keys).toList()

    fun changeLanguage(language: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LANGUAGE_KEY, language).apply()
        setLanguage(language)
    }

    fun changeTheme(theme: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(THEME_KEY, theme).apply()
    }

    fun getSavedPreferences(): SavedPreferencesItem {
        val theme = getTheme()
        val language = getLanguage()
        return SavedPreferencesItem(
            theme = theme,
            language = language,
            themeTitle = getThemeTitle(theme),
            languageTitle = getLanguageTitle(language)
        )
    }

    fun setLanguage(language: String = getLanguage()) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        onSharedPreferenceChangeListener = listener
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnChangeListener() {
        PreferenceManager.getDefaultSharedPreferences(context)
            .unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    private fun getTheme() =
        PreferenceManager.getDefaultSharedPreferences(context).getString(THEME_KEY, THEME_DEFAULT)!!

    private fun getLanguage() =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(LANGUAGE_KEY, getDefaultLanguage(context))!!

    private fun getThemeTitle(key: String): String =
        context.resources.getStringArray(R.array.Themes).toList()[getThemesKeys().indexOf(key)]

    private fun getLanguageTitle(key: String): String =
        context.resources.getStringArray(R.array.Languages).toList()[getLanguagesKeys().indexOf(key)]

    private fun getDefaultLanguage(context: Context) = context.getString(R.string.default_language)
}