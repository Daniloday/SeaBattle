package com.missclick.seabattle.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.missclick.seabattle.data.Constants
import com.missclick.seabattle.data.remote.dto.CellDto
import com.missclick.seabattle.data.remote.dto.GameDto
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.random.Random



class FireStore @Inject constructor() {

    private val db by lazy { Firebase.firestore }

    fun deleteRoom(code: String){
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update(
                mapOf(
                    "ownerCells" to FieldValue.delete(),
                    "friendCells" to FieldValue.delete(),
                    "friendIsConnected" to FieldValue.delete(),
                    "ownerIsConnected" to FieldValue.delete(),
                    "ownerIsReady" to FieldValue.delete(),
                    "friendIsReady" to FieldValue.delete(),
                    "moveOwner" to FieldValue.delete(),
                )
            )

        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .delete()

    }

    fun connect(code: String, isOwner: Boolean) = callbackFlow{
        println("here")
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update(
                if (isOwner){
                    mapOf(
                        "ownerIsReady" to false,
                        "friendIsReady" to false,
                        "ownerIsConnected" to true
                    )
                }else{
                    mapOf(
                        "ownerIsReady" to false,
                        "friendIsReady" to false,
                        "friendIsConnected" to true
                    )
                }
            )
            .addOnSuccessListener {
                println("here succiess")
                trySend(null)
            }
            .addOnFailureListener {
                println("here errer ${it.message}")
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




    fun setReady(code : String, isOwner : Boolean, cells : List<CellDto>) {
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update(
                if (isOwner){
                    mapOf(
                        "ownerCells" to cells,
                        "ownerIsReady" to true,
                        "ownerIsConnected" to false,
                        "friendIsConnected" to false
                    )
                }else{
                    mapOf(
                        "friendCells" to cells,
                        "friendIsReady" to true,
                        "ownerIsConnected" to false,
                        "friendIsConnected" to false
                    )
                }

            )

    }

    fun doStep(code : String, isOwner : Boolean, cells : List<CellDto>, change : Boolean) {
        val map = if (!isOwner){
            mutableMapOf<String,Any>(
                "ownerCells" to cells,
            )
        }else{
            mutableMapOf<String, Any>(
                "friendCells" to cells,
            )
        }

        if (change){
            map["moveOwner"] = !isOwner
        }


        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update(
                map
            )

    }



}