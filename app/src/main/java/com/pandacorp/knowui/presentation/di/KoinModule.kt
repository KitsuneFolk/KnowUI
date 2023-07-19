package com.pandacorp.knowui.presentation.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pandacorp.knowui.data.mappers.FactMapper
import com.pandacorp.knowui.data.repository.CustomSharedPreferencesImpl
import com.pandacorp.knowui.data.repository.FactsRepositoryImpl
import com.pandacorp.knowui.domain.repository.CustomSharedPreferences
import com.pandacorp.knowui.domain.repository.FactsRepository
import com.pandacorp.knowui.presentation.viewmodel.CurrentFactViewModel
import com.pandacorp.knowui.presentation.viewmodel.FactsViewModel
import com.pandacorp.knowui.presentation.viewmodel.PreferencesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinModule = module {
    singleOf(::FactMapper)
    single<CustomSharedPreferences> { CustomSharedPreferencesImpl(get()) }
    single<FactsRepository> { FactsRepositoryImpl(get(), get(), get()) }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }

    viewModelOf(::PreferencesViewModel)
    viewModelOf(::FactsViewModel)
    viewModelOf(::CurrentFactViewModel)
}