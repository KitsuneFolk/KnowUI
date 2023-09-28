package com.pandacorp.knowui.domain.models

sealed class AuthState(val error: String? = null) {
    class Success : AuthState()

    class Error(error: String?) : AuthState(error = error)
}