package com.fitworkgym.data.repository

import com.fitworkgym.data.model.Trainer

class TrainerRepository(
    private val dataSource: TrainerDataSource
) {

    suspend fun register(trainer: Trainer) = dataSource.register(trainer)

}