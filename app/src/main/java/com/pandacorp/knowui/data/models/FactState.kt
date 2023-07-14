package com.pandacorp.knowui.data.models

sealed class FactState<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : FactState<T>(data = data)
    class Error<T>(error: String, data: T? = null) : FactState<T>(error = error, data = data)
}