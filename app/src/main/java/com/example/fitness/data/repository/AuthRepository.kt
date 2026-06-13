package com.example.fitness.data.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.fitness.data.local.dao.UserDao
import com.example.fitness.data.local.entity.UserEntity
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun register(name: String, email: String, password: String): Result<Long> {
        val cleanEmail = email.trim().lowercase()
        if (userDao.findByEmail(cleanEmail) != null) {
            return Result.failure(Exception("Email sudah terdaftar"))
        }
        val id = userDao.insert(
            UserEntity(
                name = name.trim(),
                email = cleanEmail,
                passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray()),
                role = "USER"
            )
        )
        return Result.success(id)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = userDao.findByEmail(email.trim().lowercase())
            ?: return Result.failure(Exception("Email tidak ditemukan"))
        val verified = BCrypt.verifyer()
            .verify(password.toCharArray(), user.passwordHash).verified
        return if (verified) Result.success(user)
        else Result.failure(Exception("Password salah"))
    }
}