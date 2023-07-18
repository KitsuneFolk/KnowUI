package com.pandacorp.knowui.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pandacorp.knowui.domain.models.FactItem

class CurrentFactViewModel : ViewModel() {
    private val _fact = mutableStateOf(FactItem())
    val fact: State<FactItem> = _fact

    fun setFact(fact: FactItem) {
        _fact.value = fact
    }
}