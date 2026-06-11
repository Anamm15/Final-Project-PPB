package com.example.fitness.data.repository

import androidx.room.withTransaction
import com.example.fitness.data.local.database.AppDatabase
import com.example.fitness.data.local.dao.ActivityDao
import com.example.fitness.data.local.dao.RedemptionDao
import com.example.fitness.data.local.dao.RewardDao
import com.example.fitness.data.local.entity.RedemptionEntity
import com.example.fitness.data.local.entity.RewardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

sealed interface RedeemResult {
    data object Success : RedeemResult
    data object InsufficientPoints : RedeemResult
    data object OutOfStock : RedeemResult
    data class Error(val message: String) : RedeemResult
}

class RewardRepository @Inject constructor(
    private val db: AppDatabase,
    private val rewardDao: RewardDao,
    private val redemptionDao: RedemptionDao,
    private val activityDao: ActivityDao
) {
    fun observeActiveRewards(): Flow<List<RewardEntity>> =
        rewardDao.observeActiveRewards()

    fun observePointBalance(userId: Long): Flow<Int> =
        combine(
            activityDao.observeEarnedPoints(userId),
            redemptionDao.observeSpentPoints(userId)
        ) { earned, spent -> earned - spent }

    suspend fun redeem(userId: Long, rewardId: Long): RedeemResult {
        val reward = rewardDao.findById(rewardId)
            ?: return RedeemResult.Error("Reward tidak ditemukan")
        if (reward.stock <= 0) return RedeemResult.OutOfStock

        val balance = observePointBalance(userId).first()
        if (balance < reward.pointCost) return RedeemResult.InsufficientPoints

        return try {
            db.withTransaction {
                val updated = rewardDao.decrementStock(rewardId)   // WHERE stock > 0
                if (updated == 0) throw IllegalStateException("Stok habis")
                redemptionDao.insert(
                    RedemptionEntity(
                        userId = userId,
                        rewardId = rewardId,
                        pointsSpent = reward.pointCost
                    )
                )
            }
            RedeemResult.Success
        } catch (e: Exception) {
            RedeemResult.Error(e.message ?: "Gagal menukar reward")
        }
    }
}