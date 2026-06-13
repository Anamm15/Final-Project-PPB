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
import kotlinx.coroutines.flow.map
import java.time.LocalDate
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

    fun observeTodayActivities(userId: Long): Flow<List<ActivityEntity>> =
        activityDao.observeTodayActivities(userId)

    fun observeHasActivityToday(userId: Long): Flow<Boolean> =
        activityDao.observeTodayCompletedCount(userId).map { it > 0 }

    fun observeDailyStreak(userId: Long): Flow<Int> =
        activityDao.observeActivityDates(userId).map { dates ->
            calculateStreak(dates)
        }

    private fun calculateStreak(activityDates: List<String>): Int {
        if (activityDates.isEmpty()) return 0

        var streak = 0
        var currentDate = LocalDate.now()

        for (dateStr in activityDates) {
            val activityDate = LocalDate.parse(dateStr)
            if (activityDate == currentDate) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else if (activityDate == currentDate) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }
}