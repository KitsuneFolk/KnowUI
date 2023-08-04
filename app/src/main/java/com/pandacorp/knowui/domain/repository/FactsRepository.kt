package com.pandacorp.knowui.domain.repository

import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.domain.models.FactState
import kotlinx.coroutines.flow.Flow

interface FactsRepository {
    fun getFacts(limit: Long): Flow<FactState>
    fun loadMoreFacts(limit: Long): Flow<FactState>
    suspend fun loadImagesForFacts(facts: List<FactItem>): List<FactItem>
}
