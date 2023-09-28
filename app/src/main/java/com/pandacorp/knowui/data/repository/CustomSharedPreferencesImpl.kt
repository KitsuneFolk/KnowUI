package com.pandacorp.knowui.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.jintin.preferencesextension.liveData
import com.pandacorp.knowui.R
import com.pandacorp.knowui.domain.repository.CustomSharedPreferences
import com.pandacorp.knowui.utils.Constants

class CustomSharedPreferencesImpl(private val context: Context) : CustomSharedPreferences {
    companion object {
        fun getThemesKeys(context: Context): List<String> = context.resources.getStringArray(R.array.Themes_keys).toList()

        fun getLanguagesKeys(context: Context): List<String> = context.resources.getStringArray(R.array.Languages_keys).toList()
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun setLanguage(language: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }

    override fun writeLanguage(language: String) {
        sharedPreferences.edit().putString(Constants.Preferences.LANGUAGE_KEY, language).apply()
    }

    override fun writeTheme(theme: String) {
        sharedPreferences.edit().putString(Constants.Preferences.THEME_KEY, theme).apply()
    }

    override fun getThemeLivedata(): LiveData<String> = sharedPreferences.liveData(Constants.Preferences.THEME_KEY)

    override fun getLanguageLivedata(): LiveData<String> = sharedPreferences.liveData(Constants.Preferences.LANGUAGE_KEY)

    override fun getTheme(): String {
        val defaultTheme = Constants.Preferences.THEME_DEFAULT
        return sharedPreferences.getString(Constants.Preferences.THEME_KEY, defaultTheme) ?: defaultTheme
    }

    override fun getLanguage(): String {
        val defaultLanguage = getDefaultLanguage()
        return sharedPreferences.getString(Constants.Preferences.LANGUAGE_KEY, defaultLanguage) ?: defaultLanguage
    }

    private fun getDefaultLanguage() = context.getString(R.string.default_language)
}