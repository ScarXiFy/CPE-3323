package com.usc.cems.data.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, fullname: String, password: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
}
