package com.example.fitness.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitness.data.local.dao.*
import com.example.fitness.data.local.entity.*


@Database(
    entities = [
        UserEntity::class,
        MembershipPackageEntity::class,
        MembershipEntity::class,
        PaymentEntity::class,
        ActivityEntity::class,
        RewardEntity::class,
        RedemptionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun membershipDao(): MembershipDao
    abstract fun paymentDao(): PaymentDao
    abstract fun activityDao(): ActivityDao
    abstract fun rewardDao(): RewardDao
    abstract fun redemptionDao(): RedemptionDao
    abstract fun adminDao(): AdminDao
}