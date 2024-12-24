package com.fitworkgym.data.repository

import com.fitworkgym.data.model.Trainer

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TrainerDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun register(trainer: Trainer): Result<String> {
        return try {

            val userMap = mapOf(
                "name" to trainer.name,
                "schedule" to trainer.schedule,
                "email" to trainer.email,
                "image" to trainer.image,
                "phone" to trainer.phone

            )

            firestore.collection("trainers").add(userMap).addOnSuccessListener {
                Result.Success("Trainer Success")
            }.addOnFailureListener {
                Result.Failure(it)
            }.await()

            Result.Success("Trainers Success")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

}