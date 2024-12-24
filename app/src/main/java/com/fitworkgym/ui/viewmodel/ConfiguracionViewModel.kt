package com.fitworkgym.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitworkgym.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ConfiguracionViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    init {
        fetchUserName()
    }

    private fun fetchUserName() {
        viewModelScope.launch {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.let { user ->
                    val userDoc = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.uid)
                        .get()
                        .await()

                    val name = userDoc.getString("name") ?: "Usuario"
                    _name.value = name
                }
            } catch (e: Exception) {
                _name.value = "Error al obtener nombre"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
             authRepository.logout()
        }
    }

}