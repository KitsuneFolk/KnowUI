package com.pandacorp.knowui.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.pandacorp.knowui.R
import com.pandacorp.knowui.domain.models.AuthState
import com.pandacorp.knowui.domain.repository.AuthRepository
import com.pandacorp.knowui.utils.Validation

class LoginViewModel(private val application: Application, private val authRepository: AuthRepository) :
    AndroidViewModel(application) {
    var isSigned by mutableStateOf(authRepository.isSigned())
    var showSignIn by mutableStateOf(true) // Is SignIn enabled, else SignUp
    var emailErrorMessage: String? by mutableStateOf(null)
    var email by mutableStateOf("")
    var passwordErrorMessage: String? by mutableStateOf(null)
    var password by mutableStateOf("")
    var isPasswordVisible by mutableStateOf(false)
    var authErrorMessage: String? by mutableStateOf(null)

    fun validatePassword() {
        passwordErrorMessage = when {
            password.isEmpty() -> application.getString(R.string.emptyPassword)
            password.length < Validation.MIN_PASSWORD_LENGTH -> application.getString(R.string.shortPassword)
            else -> null
        }
    }

    fun validateEmail() {
        emailErrorMessage = when {
            email.isBlank() -> application.getString(R.string.emptyEmail)
            !Validation.isValidEmail(email) -> application.getString(R.string.invalidEmail)
            else -> null
        }
    }

    fun signInAnonymously(onResult: (AuthState) -> Unit) {
        authRepository.signInAnonymously(onResult)
    }

    fun signIn(onResult: (AuthState) -> Unit) {
        if (emailErrorMessage != null || passwordErrorMessage != null) return
        authRepository.signIn(email, password) {
            authErrorMessage = it.error
            onResult(it)
        }
    }

    fun signUp(onResult: (AuthState) -> Unit) {
        if (emailErrorMessage != null || passwordErrorMessage != null) return
        authRepository.signUp(email, password) {
            authErrorMessage = it.error
            onResult(it)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}