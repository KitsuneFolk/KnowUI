package com.pandacorp.knowui.domain.repository

import com.pandacorp.knowui.data.models.FactState
import com.pandacorp.knowui.domain.models.FactItem
import kotlinx.coroutines.flow.Flow

interface FactsRepository {
    fun getFacts(): Flow<FactState<List<FactItem>>>
}
