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
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.random.Random



class FireStore @Inject constructor() {

    private val db by lazy { Firebase.firestore }


    fun newCreateRoom(result : (Resource<String>) -> Unit){
        val code = (10000..99999).random(Random(System.currentTimeMillis())).toString()
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code).set(
           GameDto()
        ).addOnFailureListener {
            result(Resource.Error(it.message.toString()))
        }.addOnSuccessListener {
            result(Resource.Success(code))
        }
    }

    fun newObserve(code : String, result : (Resource<GameDto>) -> Unit){
        val docRef = db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                result(Resource.Error(e.message.toString()))
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {

                val data = snapshot.data

                result(Resource.Success(Gson().fromJson(data.toString(), GameDto::class.java)))

            } else {
                result(
                    Resource.Error("Snapshot is null or empty")
                )
            }
        }
    }
    fun newConnect(code : String, result : (Resource<Nothing?>) -> Unit){
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(code)
            .update("friendIsConnected", true)
            .addOnSuccessListener {
               result(Resource.Success(null))
            }
            .addOnFailureListener {
                result(Resource.Error(it.message.toString()))
            }
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