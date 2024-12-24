package com.fitworkgym.data.repository


import android.util.Log
import com.fitworkgym.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun login(email: String, password: String): Result<String> {

        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.Success("Login Success")
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun register(user: User): Result<String> {

        return try {
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()

            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("User not found")

            val userMap = hashMapOf(
                "name" to (user.name ?: "No name provided"),
                "email" to user.email,
                "phoneNumber" to (user.phoneNumber ?: -1), // -1 como valor predeterminado si el teléfono no está disponible
                "createdAt" to (user.createdAt ?: Timestamp.now())
            )



            firestore.collection("users").document(userId).set(userMap).await()

            Result.Success("Register Success")
        } catch (e: Exception) {
            Log.e("AuthDataSource", "Firestore error: ${e.message}")
            Result.Failure(e)
        }

    }

    fun logout(): Result<String> {
        return try {
            firebaseAuth.signOut()
            Result.Success("Logout Success")
        } catch (e: Exception) {
            Result.Failure(e)
        }

    }

}
