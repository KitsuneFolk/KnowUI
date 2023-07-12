package com.pandacorp.knowui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pandacorp.knowui.domain.models.SavedPreferencesItem
import com.pandacorp.knowui.domain.repository.CustomSharedPreferences

class PreferencesViewModel(private val customSharedPreferences: CustomSharedPreferences) : ViewModel() {
    val themeLiveData = customSharedPreferences.getThemeLivedata()
    val languageLiveData = customSharedPreferences.getLanguageLivedata()

    fun changeLanguage(language: String) {
        customSharedPreferences.writeLanguage(language)
        customSharedPreferences.setLanguage(language)
    }

    fun setLanguage(language: String) {
        customSharedPreferences.setLanguage(language)
    }

    fun changeTheme(theme: String) {
        customSharedPreferences.writeTheme(theme)
    }

    fun getSavedPreferences(): SavedPreferencesItem = customSharedPreferences.getSavedPreferences()

}