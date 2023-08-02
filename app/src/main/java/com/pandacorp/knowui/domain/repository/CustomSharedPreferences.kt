package com.pandacorp.knowui.domain.repository

import androidx.lifecycle.LiveData

interface CustomSharedPreferences {
    fun getTheme(): String

    fun getLanguage(): String

    fun getLanguageLivedata(): LiveData<String>

    fun getThemeLivedata(): LiveData<String>

    fun writeTheme(theme: String)

    fun writeLanguage(language: String)

    fun setLanguage(language: String)
}