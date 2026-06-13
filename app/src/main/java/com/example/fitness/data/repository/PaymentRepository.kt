package com.example.fitness.data.repository

import androidx.room.withTransaction
import com.example.fitness.data.local.database.AppDatabase
import com.example.fitness.data.local.dao.MembershipDao
import com.example.fitness.data.local.dao.PaymentDao
import com.example.fitness.data.local.entity.MembershipEntity
import com.example.fitness.data.local.entity.PaymentEntity
import com.example.fitness.viewmodel.MembershipTier
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val db: AppDatabase,
    private val membershipDao: MembershipDao,
    private val paymentDao: PaymentDao
) {

    suspend fun pay(userId: Long, tier: MembershipTier, method: String): Result<Long> {
        return try {
            val membershipId = db.withTransaction {
                val now = System.currentTimeMillis()
                val end = now + tier.durationDays.toLong() * 24L * 60L * 60L * 1000L

                val mId = membershipDao.insertMembership(
                    MembershipEntity(
                        userId = userId,
                        packageId = tier.id,
                        startDate = now,
                        endDate = end,
                        status = "ACTIVE"
                    )
                )
                paymentDao.insert(
                    PaymentEntity(
                        userId = userId,
                        membershipId = mId,
                        amount = tier.price.toDouble(),
                        method = method,
                        status = "SUCCESS"   // langsung sukses (simulasi)
                    )
                )
                mId
            }
            Result.success(membershipId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}