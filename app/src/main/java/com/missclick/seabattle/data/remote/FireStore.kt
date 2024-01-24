package com.missclick.seabattle.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.missclick.seabattle.data.Constants
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Field
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class FireStore @Inject constructor() {

    private val db by lazy { Firebase.firestore }

    fun createRoom(codeCallback: (String) -> Unit) {

        val codeRandom = (10000..99999).random(Random(System.currentTimeMillis())).toString()
        db.collection(Constants.FIRE_STORE_COLLECTION_NAME).document(codeRandom).set(
            Field(
                cells = listOf<List<Cell>>(),
                code = codeRandom,
                isConnected1 = true,
                isConnected2 = false,
                isReady1 = false,
                isReady2 = false,
                moveFirst = true
            )
        ).addOnSuccessListener {
            codeCallback(codeRandom)
        }.addOnFailureListener {
//                throw it
        }

    }

    fun connectById(code: String) {

        val docRef = db.collection("games").document(code)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("firebase: $e")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                println("data")
                println(snapshot.data)
            } else {
                println("firebase: error1")
            }
        }

    }


}