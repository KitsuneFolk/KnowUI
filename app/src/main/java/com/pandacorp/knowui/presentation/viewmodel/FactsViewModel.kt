package com.pandacorp.knowui.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandacorp.knowui.data.models.FactState
import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.domain.repository.FactsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {
    private val _facts = mutableStateOf<List<FactItem>>(emptyList())
    val facts: State<List<FactItem>> = _facts

    init {
        update()
    }

    private fun update() {
        factsRepository.getFacts().onEach { state ->
            when (state) {
                is FactState.Success -> _facts.value = state.data!!

                is FactState.Error -> _facts.value = emptyList()
            }

        }.launchIn(viewModelScope)
    }
}