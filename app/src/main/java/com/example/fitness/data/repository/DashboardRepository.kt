package com.example.fitness.data.repository

import com.example.fitness.data.local.dao.ActivityDao
import com.example.fitness.data.local.dao.MembershipDao
import com.example.fitness.data.local.dao.RedemptionDao
import com.example.fitness.data.local.dao.UserDao
import com.example.fitness.data.local.entity.ActivityEntity
import com.example.fitness.data.local.entity.MembershipEntity
import com.example.fitness.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val userDao: UserDao,
    private val membershipDao: MembershipDao,
    private val activityDao: ActivityDao,
    private val redemptionDao: RedemptionDao
) {
    fun observeUser(userId: Long): Flow<UserEntity?> =
        userDao.observeById(userId)

    fun observeActiveMembership(userId: Long): Flow<MembershipEntity?> =
        membershipDao.observeActiveMembership(userId)

    fun observePointBalance(userId: Long): Flow<Int> =
        combine(
            activityDao.observeEarnedPoints(userId),
            redemptionDao.observeSpentPoints(userId)
        ) { earned, spent -> earned - spent }

    fun observeRecentActivities(userId: Long): Flow<List<ActivityEntity>> =
        activityDao.observeByUser(userId)
}