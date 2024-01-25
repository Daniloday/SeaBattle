package com.missclick.seabattle.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.data.Constants
import com.missclick.seabattle.data.remote.dto.CellDto
import com.missclick.seabattle.data.remote.dto.GameDto
import com.missclick.seabattle.domain.model.Game
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.random.Random



class FireStore @Inject constructor() {

    private val db by lazy { Firebase.firestore }

    fun connect(code : String) = callbackFlow<Nothing?>{
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update("friendIsConnected", true)
            .addOnSuccessListener {
                trySend(null)
            }
            .addOnFailureListener {
                close(it)
            }
        awaitClose()
    }

    fun observe(code : String) = callbackFlow<GameDto>{
        val docRef = db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {

                val data = snapshot.data
                val data2 = Gson().fromJson(data.toString(), GameDto::class.java)
                trySend(data2)

            } else {
                close(Throwable("Snapshot is null or empty"))
            }
        }
        awaitClose()
    }

    fun createRoom() = callbackFlow<String>{
        val code = (10000..99999).random(Random(System.currentTimeMillis())).toString()
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code).set(
           GameDto()
        ).addOnFailureListener {
           close(it)
        }.addOnSuccessListener {
            trySend(code)
        }
        awaitClose()
    }




    fun setReady(code : String, isOwner : Boolean, cells : List<CellDto>){
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update(
                if (isOwner){
                    mapOf(
                        "ownerCells" to cells,
                        "ownerIsReady" to true
                    )
                }else{
                    mapOf(
                        "friendCells" to cells,
                        "friendIsReady" to true
                    )
                }

            )

    }

    fun doStep(code : String, isOwner : Boolean, cells : List<CellDto>){
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update(
                if (!isOwner){
                    mapOf(
                        "ownerCells" to cells,
                        "moveOwner" to true
                    )
                }else{
                    mapOf(
                        "friendCells" to cells,
                        "moveOwner" to false
                    )
                }

            )

    }



}