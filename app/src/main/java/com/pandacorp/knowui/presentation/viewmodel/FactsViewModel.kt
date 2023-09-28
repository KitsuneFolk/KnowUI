package com.pandacorp.knowui.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.domain.models.FactState
import com.pandacorp.knowui.domain.repository.FactsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {
    companion object {
        private const val factsLimit: Long = 20
    }

    private val _facts = mutableStateOf<FactState>(FactState.Loading())
    val facts: State<FactState> = _facts
    val isStopLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        getFacts()
    }

    private fun getFacts(limit: Long = factsLimit) {
        factsRepository.getFacts(limit = limit).onEach { state ->
            _facts.value =
                when (state) {
                    is FactState.Success -> {
                        // Now load the images for the fetched facts
                        loadImages(state.data ?: emptyList())
                        FactState.Success(data = state.data ?: emptyList())
                    }

                    is FactState.Error -> FactState.Error(error = state.error)

                    else -> throw IllegalArgumentException("Unexpected state: $state")
                }
        }.launchIn(viewModelScope)
    }

    // Function to load images for the fetched facts
    private fun loadImages(facts: List<FactItem>) {
        viewModelScope.launch {
            val factsWithImages = factsRepository.loadImagesForFacts(facts)
            _facts.value = FactState.Success(data = factsWithImages)
        }
    }

    fun loadMoreFacts(limit: Long = factsLimit) {
        if (isStopLoading.value) return
        factsRepository.loadMoreFacts(limit = limit).onEach { state ->
            _facts.value =
                when (state) {
                    is FactState.Success -> {
                        val newData = _facts.value.data?.plus(state.data ?: emptyList()) ?: state.data ?: emptyList()
                        loadImages(newData)
                        if (state.data.isNullOrEmpty()) isStopLoading.value = true
                        FactState.Success(data = newData)
                    }

                    is FactState.Error -> FactState.Error(error = state.error)

                    else -> throw IllegalArgumentException("Unexpected state: $state")
                }
        }.launchIn(viewModelScope)
    }
}