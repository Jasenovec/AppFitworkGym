package com.fitworkgym.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitworkgym.data.model.Trainer
import com.fitworkgym.data.repository.TrainerRepository
import kotlinx.coroutines.launch

class TrainerViewModel(
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val _trainer = MutableLiveData<TrainerState>(TrainerState.Idle)
    val trainer: LiveData<TrainerState> = _trainer

    fun registerTrainer(trainer: Trainer) {
        _trainer.value = TrainerState.Loading
        viewModelScope.launch {
            val result = trainerRepository.register(trainer)
            _trainer.value = when {
                result.isSuccess -> TrainerState.Success(result.getOrNull() ?: "Registro exitoso")
                else -> TrainerState.Error(
                    result.exceptionOrNull()?.message ?: "Error al registrar"
                )
            }
        }
    }


}

sealed class TrainerState {
    data object Idle : TrainerState()
    data object Loading : TrainerState()
    data class Success(val message: String) : TrainerState()
    data class Error(val error: String) : TrainerState()
}