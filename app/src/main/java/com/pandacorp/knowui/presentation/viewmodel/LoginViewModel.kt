package com.pandacorp.knowui.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.pandacorp.knowui.R
import com.pandacorp.knowui.utils.Validation

class LoginViewModel(private val application: Application) : AndroidViewModel(application) {
    var isSigned by mutableStateOf(false)
    var isSignIn by mutableStateOf(true) // Is SignIn enabled, else SignUp
    var isSkipped by mutableStateOf(false)
    var emailErrorMessage: String? by mutableStateOf(null)
    var email by mutableStateOf("")
    var passwordErrorMessage: String? by mutableStateOf(null)
    var password by mutableStateOf("")
    var isPasswordVisible by mutableStateOf(false)

    fun validatePassword() {
        passwordErrorMessage = when {
            password.isEmpty() -> application.getString(R.string.emptyPassword)
            password.length < 6 -> application.getString(R.string.shortPassword)
            else -> null
        }
    }

    fun validateEmail() {
        emailErrorMessage = when {
            email.isEmpty() -> application.getString(R.string.emptyEmail)
            !Validation.isValidEmail(email) -> application.getString(R.string.invalidEmail)
            else -> null
        }
    }
}