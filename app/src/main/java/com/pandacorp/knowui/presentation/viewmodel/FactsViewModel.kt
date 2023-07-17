package com.pandacorp.knowui.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandacorp.knowui.data.models.FactState
import com.pandacorp.knowui.domain.repository.FactsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {
    companion object {
        private const val factsLimit: Long = 20
    }

    private val _facts = mutableStateOf<FactState>(FactState.Loading())
    val facts: State<FactState> = _facts
    private val isStopLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        getFacts()
    }

    private fun getFacts(limit: Long = factsLimit) {
        factsRepository.getFacts(limit = limit).onEach { state ->
            _facts.value = when (state) {
                is FactState.Success -> FactState.Success(data = state.data!!)

                is FactState.Error -> FactState.Error(error = state.error)

                else -> throw IllegalArgumentException("Unexpected state: $state")
            }

        }.launchIn(viewModelScope)
    }

    fun loadMoreFacts(limit: Long = factsLimit) {
        if (isStopLoading.value) return
        factsRepository.loadMoreFacts(limit = limit).onEach { state ->
            _facts.value = when (state) {
                is FactState.Success -> {
                    val newData = _facts.value.data?.plus(state.data!!) ?: state.data!!
                    if (state.data.isNullOrEmpty()) {
                        isStopLoading.value = true
                        FactState.Success(data = newData)
                    }
                    else FactState.Success(data = newData)
                }

                is FactState.Error -> FactState.Error(error = state.error)

                else -> throw IllegalArgumentException("Unexpected state: $state")
            }

        }.launchIn(viewModelScope)
    }
}