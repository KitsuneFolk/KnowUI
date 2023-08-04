package com.pandacorp.knowui.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.pandacorp.knowui.domain.models.AuthState
import com.pandacorp.knowui.domain.repository.AuthRepository

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {
    override fun isSigned(): Boolean = auth.currentUser != null

    override fun signInAnonymously(onResult: (AuthState) -> Unit) {
        auth.signInAnonymously().addOnCompleteListener {
            if (it.isSuccessful) {
                onResult(AuthState.Success())
            } else onResult(AuthState.Error(it.exception?.localizedMessage ?: it.exception?.message))
        }
    }

    override fun signIn(email: String, password: String, onResult: (AuthState) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) onResult(AuthState.Success())
            else onResult(AuthState.Error(it.exception?.localizedMessage ?: it.exception?.message))
        }
    }

    override fun signUp(email: String, password: String, onResult: (AuthState) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) onResult(AuthState.Success())
            else onResult(AuthState.Error(it.exception?.localizedMessage ?: it.exception?.message))
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}