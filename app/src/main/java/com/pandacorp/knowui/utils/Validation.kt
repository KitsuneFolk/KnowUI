package com.pandacorp.knowui.utils

object Validation {
    private const val MIN_PASSWORD_LENGTH = 6

    fun isValidEmail(email: String): Boolean = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.isNotBlank() && password.length >= MIN_PASSWORD_LENGTH

    fun isValidInput(email: String?, password: String?): Boolean {
        return if (email == null || password == null) false
        else isValidEmail(email) && isValidPassword(password)
    }
}