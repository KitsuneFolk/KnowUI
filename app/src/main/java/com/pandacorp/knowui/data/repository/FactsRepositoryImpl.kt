package com.pandacorp.knowui.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.pandacorp.knowui.data.mappers.FactMapper
import com.pandacorp.knowui.data.models.FactDataItem
import com.pandacorp.knowui.data.models.FactState
import com.pandacorp.knowui.domain.repository.FactsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FactsRepositoryImpl(
    fireStore: FirebaseFirestore,
    private val mapper: FactMapper,
) : FactsRepository {
    private var lastVisible: DocumentSnapshot? = null
    private val baseQuery = fireStore.collection("Facts")
        .orderBy(FieldPath.documentId()) // Order by id

    override fun getFacts(limit: Long): Flow<FactState> = callbackFlow {
        baseQuery.limit(limit)

        val snapshotListener = baseQuery.addSnapshotListener { snapshot, exception ->
            val response = if (exception == null) {
                snapshot?.let { querySnapshot ->
                    val list: List<FactDataItem> = querySnapshot.toObjects(FactDataItem::class.java)
                    val mappedList = mapper.listToFactItem(list)
                    lastVisible = querySnapshot.documents.lastOrNull()

                    FactState.Success(mappedList)
                } ?: FactState.Error("Empty list")
            } else FactState.Error(exception.message ?: "Unknown error")

            trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
            close()
        }
    }

    override fun loadMoreFacts(limit: Long) : Flow<FactState> = callbackFlow {
        baseQuery.limit(limit)
        lastVisible?.let {
            val query = baseQuery.startAfter(it.id)
            val additionalSnapshot = query.get().await()
            val additionalList: List<FactDataItem> = additionalSnapshot.toObjects(FactDataItem::class.java)
            val additionalMappedList = mapper.listToFactItem(additionalList)
            lastVisible = additionalSnapshot.documents.lastOrNull()

            val additionalResponse = if (additionalMappedList.isNotEmpty())
                FactState.Success(additionalMappedList)
            else FactState.Success(emptyList())

            trySend(additionalResponse)

            awaitClose {
                close()
            }
        }
    }
}