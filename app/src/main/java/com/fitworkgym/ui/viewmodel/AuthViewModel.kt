package com.fitworkgym.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitworkgym.data.model.User

import com.fitworkgym.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState


    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = loginRepository.login(email, password)
            _authState.value = when {
                result.isSuccess -> AuthState.Success(result.getOrNull() ?: "Inicio exitoso")
                else -> AuthState.Error(
                    result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                )
            }
        }
    }

    fun register(user: User) {
        if (!isValidUser(user)) {
            _authState.value = AuthState.Error("Datos inválidos")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = loginRepository.register(user)
            _authState.value = when {
                result.isSuccess -> AuthState.Success(result.getOrNull() ?: "Registro exitoso")
                else -> AuthState.Error(result.exceptionOrNull()?.message ?: "Error al registrar")
            }
        }
    }
    private fun isValidUser(user: User): Boolean {
        return user.email.isNotEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches() &&
                user.password.isNotEmpty() &&
                user.password.length >= 6 &&
                (user.phoneNumber?.toString()?.matches(Regex("^\\d{9}$")) ?: false)
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val error: String) : AuthState()
}