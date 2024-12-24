package com.fitworkgym.data.model

import com.google.firebase.Timestamp

data class User(
    val name: String? = null,
    val email: String,
    val password: String,
    val phoneNumber: Int? = null,
    val createdAt: Timestamp? = null
)