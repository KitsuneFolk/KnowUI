package com.pandacorp.knowui.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pandacorp.knowui.data.mappers.FactMapper
import com.pandacorp.knowui.data.models.FactDataItem
import com.pandacorp.knowui.data.models.FactState
import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.domain.repository.FactsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FactsRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val mapper: FactMapper,
) : FactsRepository {

    override fun getFacts(): Flow<FactState<List<FactItem>>> = callbackFlow {
        fireStore.collection("Facts")
            .get()
            .addOnCompleteListener { task ->
                val response = if (task.isSuccessful) {
                    val list: List<FactDataItem> = task.result?.toObjects(FactDataItem::class.java) ?: emptyList()
                    val mappedList: List<FactItem> = list.map { dataItem ->
                        mapper.toFactItem(dataItem)
                    }
                    FactState.Success(mappedList)
                } else FactState.Error(task.exception?.localizedMessage.toString())
                trySend(response).isSuccess
            }

        awaitClose {
            close()
        }
    }
}