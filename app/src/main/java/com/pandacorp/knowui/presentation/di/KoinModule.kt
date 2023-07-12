package com.pandacorp.knowui.presentation.di

import com.pandacorp.knowui.data.mappers.FactMapper
import com.pandacorp.knowui.data.repository.CustomSharedPreferencesImpl
import com.pandacorp.knowui.domain.repository.CustomSharedPreferences
import com.pandacorp.knowui.presentation.viewmodel.PreferencesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinModule = module {
    single<CustomSharedPreferences> {
        CustomSharedPreferencesImpl(get())
    }
    singleOf(::FactMapper)
    viewModelOf(::PreferencesViewModel)
}