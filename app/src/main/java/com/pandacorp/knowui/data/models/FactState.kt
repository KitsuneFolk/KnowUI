package com.pandacorp.knowui.data.models

import com.pandacorp.knowui.domain.models.FactItem

sealed class FactState(val data: List<FactItem>? = null, val error: String? = null) {
    class Success(data: List<FactItem>) : FactState(data = data)
    class Error(error: String?, data: List<FactItem>? = null) : FactState(error = error, data = data)
    class Loading : FactState()
}