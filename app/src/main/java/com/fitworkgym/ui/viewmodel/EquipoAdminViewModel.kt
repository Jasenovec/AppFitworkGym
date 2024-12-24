package com.fitworkgym.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitworkgym.data.model.Equipo
import com.fitworkgym.data.repository.EquipoRepository
import kotlinx.coroutines.launch

class EquipoAdminViewModel(
    private val equipoRepository: EquipoRepository
) : ViewModel() {

    private val _trainer = MutableLiveData<EquipoState>(EquipoState.Idle)
    val trainer: LiveData<EquipoState> = _trainer

    fun registerEquipo(equipo: Equipo) {
        _trainer.value = EquipoState.Loading
        viewModelScope.launch {
            val result = equipoRepository.register(equipo)
            _trainer.value = when {
                result.isSuccess -> EquipoState.Success(result.getOrNull() ?: "Registro exitoso")
                else -> EquipoState.Error(
                    result.exceptionOrNull()?.message ?: "Error al registrar"
                )
            }
        }
    }


}

sealed class EquipoState {
    data object Idle : EquipoState()
    data object Loading : EquipoState()
    data class Success(val message: String) : EquipoState()
    data class Error(val error: String) : EquipoState()
}