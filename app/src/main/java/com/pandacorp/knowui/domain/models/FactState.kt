package com.pandacorp.knowui.domain.models

sealed class FactState(val data: List<FactItem>? = null, val error: String? = null) {
    class Success(data: List<FactItem>) : FactState(data = data)
    class Error(error: String? = null) : FactState(error = error)
    class Loading : FactState()
}