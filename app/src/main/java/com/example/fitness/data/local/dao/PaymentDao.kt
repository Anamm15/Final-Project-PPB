package com.example.fitness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitness.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert
    suspend fun insert(payment: PaymentEntity): Long

    @Query("UPDATE payments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("SELECT * FROM payments WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUser(userId: Long): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<PaymentEntity>>       // untuk admin
}