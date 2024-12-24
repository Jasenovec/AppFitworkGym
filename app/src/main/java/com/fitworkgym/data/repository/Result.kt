package com.fitworkgym.data.repository

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = if (this is Success) data else null
    fun exceptionOrNull(): Exception? = if (this is Failure) exception else null
}