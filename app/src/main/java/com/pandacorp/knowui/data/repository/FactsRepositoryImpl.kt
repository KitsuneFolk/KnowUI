package com.pandacorp.knowui.data.repository

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pandacorp.knowui.data.mappers.FactMapper
import com.pandacorp.knowui.data.models.FactDataItem
import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.domain.models.FactState
import com.pandacorp.knowui.domain.repository.FactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FactsRepositoryImpl(
    fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val mapper: FactMapper,
) : FactsRepository {
    private var lastVisible: DocumentSnapshot? = null
    private val baseQuery =
        fireStore.collection("Facts")
            .orderBy(FieldPath.documentId()) // Order by id

    override fun getFacts(limit: Long): Flow<FactState> =
        callbackFlow {
            baseQuery.limit(limit)

            val snapshotListener =
                baseQuery.addSnapshotListener { snapshot, exception ->
                    val response =
                        if (exception == null) {
                            snapshot?.let { querySnapshot ->
                                val list: List<FactDataItem> = querySnapshot.toObjects(FactDataItem::class.java)
                                val mappedList = mapper.listToFactItem(list)

                                lastVisible = querySnapshot.documents.lastOrNull()

                                FactState.Success(mappedList)
                            } ?: FactState.Error("Empty list")
                        } else {
                            FactState.Error(exception.message ?: "Unknown error")
                        }

                    trySend(response).isSuccess
                }

            awaitClose {
                snapshotListener.remove()
                close()
            }
        }

    override fun loadMoreFacts(limit: Long): Flow<FactState> =
        callbackFlow {
            baseQuery.limit(limit)
            lastVisible?.let {
                val query = baseQuery.startAfter(it.id)
                val additionalSnapshot = query.get().await()
                val additionalList: List<FactDataItem> = additionalSnapshot.toObjects(FactDataItem::class.java)
                val additionalMappedList = mapper.listToFactItem(additionalList)
                lastVisible = additionalSnapshot.documents.lastOrNull()

                val additionalResponse =
                    if (additionalMappedList.isNotEmpty()) {
                        FactState.Success(additionalMappedList)
                    } else {
                        FactState.Success(emptyList())
                    }

                trySend(additionalResponse)

                awaitClose {
                    close()
                }
            }
        }

    override suspend fun loadImagesForFacts(facts: List<FactItem>): List<FactItem> =
        withContext(Dispatchers.IO) {
            val imageUris = mutableListOf<Uri?>()

            // Load images for each FactItem in parallel using async and awaitAll
            val deferredImageLoads =
                facts.map { factItem ->
                    loadImageFromStorage(factItem.imagePath)
                }

            deferredImageLoads.forEach { uri ->
                imageUris.add(uri)
            }

            return@withContext facts.mapIndexed { index, factItem ->
                factItem.copy(imageUri = imageUris[index])
            }
        }

    private suspend fun loadImageFromStorage(imagePath: String): Uri? {
        val storageRef = storage.getReferenceFromUrl(imagePath)

        return try {
            storageRef.downloadUrl.await()
        } catch (e: Exception) {
            null
        }
    }
}