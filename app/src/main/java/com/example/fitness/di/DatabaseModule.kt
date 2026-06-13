package com.example.fitness.di

import android.content.Context
import androidx.room.Room
import com.example.fitness.data.local.dao.ActivityDao
import com.example.fitness.data.local.dao.AdminDao
import com.example.fitness.data.local.dao.MembershipDao
import com.example.fitness.data.local.dao.PaymentDao
import com.example.fitness.data.local.dao.RedemptionDao
import com.example.fitness.data.local.dao.RewardDao
import com.example.fitness.data.local.dao.UserDao
import com.example.fitness.data.local.database.AppDatabase
import com.example.fitness.data.local.database.DatabaseSeeder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "fitmember.db")
            .fallbackToDestructiveMigration()
            .addCallback(DatabaseSeeder)
            .build()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideMembershipDao(database: AppDatabase): MembershipDao = database.membershipDao()

    @Provides
    @Singleton
    fun providePaymentDao(database: AppDatabase): PaymentDao = database.paymentDao()

    @Provides
    @Singleton
    fun provideActivityDao(database: AppDatabase): ActivityDao = database.activityDao()

    @Provides
    @Singleton
    fun provideRewardDao(database: AppDatabase): RewardDao = database.rewardDao()

    @Provides
    @Singleton
    fun provideRedemptionDao(database: AppDatabase): RedemptionDao = database.redemptionDao()

    @Provides
    @Singleton
    fun provideAdminDao(database: AppDatabase): AdminDao = database.adminDao()
}