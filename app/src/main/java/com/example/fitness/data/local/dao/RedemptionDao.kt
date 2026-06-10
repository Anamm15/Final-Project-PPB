package com.example.fitness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitness.data.local.entity.RedemptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RedemptionDao {
    @Insert suspend fun insert(redemption: RedemptionEntity): Long

    // Komponen kedua saldo poin: total poin yang sudah dibelanjakan
    @Query("""
        SELECT COALESCE(SUM(pointsSpent), 0) FROM redemptions
        WHERE userId = :userId AND status = 'COMPLETED'
    """)
    fun observeSpentPoints(userId: Long): Flow<Int>

    @Query("SELECT * FROM redemptions WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUser(userId: Long): Flow<List<RedemptionEntity>>
}