package com.fitworkgym.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitworkgym.R
import com.fitworkgym.data.model.Activities
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit


class HomeViewModel : ViewModel() {

    // LiveData para el estado de de atencion del gimnasio
    private val _estadoApertura = MutableLiveData<EstadoApertura>()
    val estadoApertura: LiveData<EstadoApertura> = _estadoApertura

    // LiveData para el estado de la membresía
    private val _estadoMembresia = MutableLiveData<String>()
    val estadoMembresia: LiveData<String> = _estadoMembresia

    fun verificarHorario() {
        val horaActual = LocalTime.now()
        val horaApertura = LocalTime.of(9, 0)   // 9:00 AM
        val horaCierre = LocalTime.of(21, 0)    // 9:00 PM

        val estado = if (horaActual.isAfter(horaApertura) && horaActual.isBefore(horaCierre)) {
            EstadoApertura("Abierto", true)
        } else {
            EstadoApertura("Cerrado", false)
        }

        _estadoApertura.value = estado
    }


    private fun calcularDiasRestantes(
        fechaRegistro: LocalDate,
        tipoMembresia: TipoMembresia
    ): Long {
        val fechaActual = LocalDate.now()
        val fechaExpiracion = fechaRegistro.plusDays(tipoMembresia.duracion)

        return ChronoUnit.DAYS.between(fechaActual, fechaExpiracion)
    }

    private fun obtenerEstadoMembresia(
        fechaRegistro: LocalDate,
        tipoMembresia: TipoMembresia
    ): String {
        val diasRestantes = calcularDiasRestantes(fechaRegistro, tipoMembresia)

        return when {
            diasRestantes > 0 -> "$diasRestantes"
            diasRestantes == 0L -> "Expira hoy"
            else -> "Expirada"
        }
    }

    fun verificarMembresia(fechaRegistro: LocalDate, tipoMembresia: TipoMembresia) {
        val estado = obtenerEstadoMembresia(fechaRegistro, tipoMembresia)
        _estadoMembresia.value = estado
    }


    // LiveData para Activities
    private val _activities = MutableLiveData<List<Activities>>().apply {
        value = listOf(
            Activities("Yoga", "John Doe", "Monday 10:00", R.drawable.logo),
            Activities("Pilates", "Jane Smith", "Tuesday 11:00", R.drawable.logo),
            Activities("Zumba", "Bob Johnson", "Wednesday 12:00", R.drawable.logo)
        )
    }
    val activities: LiveData<List<Activities>> = _activities

}

data class EstadoApertura(
    val texto: String,
    val estaAbierto: Boolean
)

enum class TipoMembresia(val duracion: Long) {
    TREINTA_DIAS(30),
    NOVENTA_DIAS(90),
    CIENTO_VEINTE_DIAS(120),
    UN_ANNO(365)
}