package com.pandacorp.knowui.presentation.di.modules

import com.pandacorp.knowui.presentation.viewmodel.CurrentFactViewModel
import com.pandacorp.knowui.presentation.viewmodel.FactsViewModel
import com.pandacorp.knowui.presentation.viewmodel.LoginViewModel
import com.pandacorp.knowui.presentation.viewmodel.PreferencesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val ViewModelModule =
    module {
        viewModelOf(::PreferencesViewModel)
        viewModelOf(::FactsViewModel)
        viewModelOf(::CurrentFactViewModel)
        viewModelOf(::LoginViewModel)
    }