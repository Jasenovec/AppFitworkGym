package com.fitworkgym.data.repository

import com.fitworkgym.data.model.Equipo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EquipoDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun register(equipo : Equipo): Result<String> {
        return try {

            val userMap = mapOf(
                "name" to equipo.name,
                "description" to equipo.description,
                "image" to equipo.image,

            )

            firestore.collection("equipos").add(userMap).addOnSuccessListener {
                Result.Success("Equipos Success")
            }.addOnFailureListener {
                Result.Failure(it)
            }.await()

            Result.Success("Equipos Success")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

}