package com.fitworkgym.data.repository

import com.fitworkgym.data.model.User

class AuthRepository(private val dataSource: AuthDataSource) {
    suspend fun login(email: String, password: String): Result<String> =
        dataSource.login(email, password)

    suspend fun register(user: User): Result<String> =
        dataSource.register(user)

    fun logout(): Result<String> = dataSource.logout()
}