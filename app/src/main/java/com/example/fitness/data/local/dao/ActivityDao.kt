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

    // Get today's activities
    @Query("""
        SELECT * FROM activities 
        WHERE userId = :userId 
        AND date(createdAt / 1000, 'unixepoch') = date('now')
        ORDER BY createdAt DESC
    """)
    fun observeTodayActivities(userId: Long): Flow<List<ActivityEntity>>

    // Count completed activities for today
    @Query("""
        SELECT COUNT(*) FROM activities 
        WHERE userId = :userId 
        AND date(createdAt / 1000, 'unixepoch') = date('now')
        AND endTime IS NOT NULL
    """)
    fun observeTodayCompletedCount(userId: Long): Flow<Int>

    // Get all activities sorted by date to calculate streak
    @Query("""
        SELECT DISTINCT date(createdAt / 1000, 'unixepoch') as activity_date
        FROM activities 
        WHERE userId = :userId 
        AND endTime IS NOT NULL
        ORDER BY createdAt DESC
    """)
    fun observeActivityDates(userId: Long): Flow<List<String>>
}