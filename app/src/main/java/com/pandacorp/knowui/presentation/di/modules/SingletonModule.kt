package com.pandacorp.knowui.presentation.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pandacorp.knowui.data.mappers.FactMapper
import com.pandacorp.knowui.data.repository.AuthRepositoryImpl
import com.pandacorp.knowui.data.repository.CustomSharedPreferencesImpl
import com.pandacorp.knowui.data.repository.FactsRepositoryImpl
import com.pandacorp.knowui.domain.repository.AuthRepository
import com.pandacorp.knowui.domain.repository.CustomSharedPreferences
import com.pandacorp.knowui.domain.repository.FactsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val SingletonModule = module {
    singleOf(::FactMapper)
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single<CustomSharedPreferences> { CustomSharedPreferencesImpl(get()) }
    single<FactsRepository> { FactsRepositoryImpl(get(), get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}