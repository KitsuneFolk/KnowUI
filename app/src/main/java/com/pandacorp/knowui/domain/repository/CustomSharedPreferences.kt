package com.pandacorp.knowui.domain.repository

import androidx.lifecycle.LiveData
import com.pandacorp.knowui.domain.models.SavedPreferencesItem

interface CustomSharedPreferences {
    fun getSavedPreferences(): SavedPreferencesItem

    fun getLanguageLivedata(): LiveData<String>

    fun getThemeLivedata(): LiveData<String>

    fun writeTheme(theme: String)

    fun writeLanguage(language: String)

    fun setLanguage(language: String)
}