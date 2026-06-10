package com.example.fitness.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitness.data.local.entity.MembershipEntity
import com.example.fitness.data.local.entity.MembershipPackageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MembershipDao {
    @Query("SELECT * FROM membership_packages WHERE isActive = 1")
    fun observePackages(): Flow<List<MembershipPackageEntity>>

    @Insert suspend fun insertPackage(pkg: MembershipPackageEntity): Long
    @Insert suspend fun insertMembership(m: MembershipEntity): Long

    @Query("SELECT * FROM memberships WHERE userId = :userId ORDER BY endDate DESC LIMIT 1")
    fun observeActiveMembership(userId: Long): Flow<MembershipEntity?>

    @Query("UPDATE memberships SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
}