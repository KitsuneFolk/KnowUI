package com.pandacorp.knowui.domain.models

sealed class FactState(val data: List<FactItem>? = null, val error: String? = null) {
    class Success(data: List<FactItem>) : FactState(data = data)
    class Error(error: String?, data: List<FactItem>? = null) : FactState(error = error, data = data)
    class Loading : FactState()
}