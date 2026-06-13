package com.example.fitness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitness.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeById(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE role = 'USER' ORDER BY createdAt DESC")
    fun observeAllMembers(): Flow<List<UserEntity>>   // untuk admin
}