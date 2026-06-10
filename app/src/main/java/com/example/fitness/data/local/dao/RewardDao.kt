package com.example.fitness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitness.data.local.entity.RewardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards WHERE isActive = 1 ORDER BY pointCost ASC")
    fun observeActiveRewards(): Flow<List<RewardEntity>>

    @Query("SELECT * FROM rewards ORDER BY id DESC")
    fun observeAll(): Flow<List<RewardEntity>>        // untuk admin

    @Query("SELECT * FROM rewards WHERE id = :id")
    suspend fun findById(id: Long): RewardEntity?

    @Insert suspend fun insert(reward: RewardEntity): Long
    @Update
    suspend fun update(reward: RewardEntity)

    // Kurangi stok hanya jika masih ada; mengembalikan jumlah baris terpengaruh (0 = gagal)
    @Query("UPDATE rewards SET stock = stock - 1 WHERE id = :id AND stock > 0")
    suspend fun decrementStock(id: Long): Int
}