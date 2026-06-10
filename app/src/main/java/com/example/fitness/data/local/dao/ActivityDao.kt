package com.example.fitness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitness.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert suspend fun insert(activity: ActivityEntity): Long

    @Query("SELECT * FROM activities WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUser(userId: Long): Flow<List<ActivityEntity>>

    // Komponen pertama saldo poin: total poin yang diperoleh
    @Query("SELECT COALESCE(SUM(pointsEarned), 0) FROM activities WHERE userId = :userId")
    fun observeEarnedPoints(userId: Long): Flow<Int>
}