package com.example.fitness.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// POJO hasil join payments + users (bukan @Entity)
data class PaymentWithUser(
    val id: Long,
    val userName: String,
    val amount: Double,
    val method: String,
    val status: String,
    val createdAt: Long
)

@Dao
interface AdminDao {

    @Query("SELECT COUNT(*) FROM users WHERE role = 'USER'")
    fun observeTotalMembers(): Flow<Int>

    @Query("SELECT COUNT(*) FROM memberships WHERE status = 'ACTIVE'")
    fun observeActiveMemberships(): Flow<Int>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'SUCCESS'")
    fun observeTotalRevenue(): Flow<Double>

    @Query("SELECT COUNT(*) FROM payments WHERE status = 'SUCCESS'")
    fun observeSuccessfulPaymentCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM activities")
    fun observeTotalActivities(): Flow<Int>

    @Query("SELECT COUNT(*) FROM redemptions WHERE status = 'COMPLETED'")
    fun observeTotalRedemptions(): Flow<Int>

    @Query(
        """
        SELECT p.id AS id, u.name AS userName, p.amount AS amount,
               p.method AS method, p.status AS status, p.createdAt AS createdAt
        FROM payments p
        INNER JOIN users u ON u.id = p.userId
        ORDER BY p.createdAt DESC
        LIMIT 10
        """
    )
    fun observeRecentPayments(): Flow<List<PaymentWithUser>>
}