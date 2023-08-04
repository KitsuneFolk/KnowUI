package com.pandacorp.knowui.domain.repository

import com.pandacorp.knowui.domain.models.AuthState

interface AuthRepository {
    fun isSigned(): Boolean
    fun isSignedAnonymously(): Boolean
    fun signInAnonymously()
    fun signIn(email: String, password: String, onResult: (AuthState) -> Unit)
    fun signUp(email: String, password: String, onResult: (AuthState) -> Unit)
    fun signOut()
}