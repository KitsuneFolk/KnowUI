package com.pandacorp.knowui.domain.repository

import com.pandacorp.knowui.data.models.FactState
import kotlinx.coroutines.flow.Flow

interface FactsRepository {
    fun getFacts(limit: Long): Flow<FactState>
    fun loadMoreFacts(limit: Long): Flow<FactState>
}
