package com.fitworkgym.data.repository

import com.fitworkgym.data.model.Equipo


class EquipoRepository(
    private val dataSource: EquipoDataSource
) {

    suspend fun register(equipo : Equipo) = dataSource.register(equipo)
}