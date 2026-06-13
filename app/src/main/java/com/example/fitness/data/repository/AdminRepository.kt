package com.example.fitness.data.repository

import com.example.fitness.data.local.dao.AdminDao
import com.example.fitness.data.local.dao.PaymentWithUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val adminDao: AdminDao
) {
    fun observeTotalMembers(): Flow<Int> = adminDao.observeTotalMembers()
    fun observeActiveMemberships(): Flow<Int> = adminDao.observeActiveMemberships()
    fun observeTotalRevenue(): Flow<Double> = adminDao.observeTotalRevenue()
    fun observeSuccessfulPaymentCount(): Flow<Int> = adminDao.observeSuccessfulPaymentCount()
    fun observeTotalActivities(): Flow<Int> = adminDao.observeTotalActivities()
    fun observeTotalRedemptions(): Flow<Int> = adminDao.observeTotalRedemptions()
    fun observeRecentPayments(): Flow<List<PaymentWithUser>> = adminDao.observeRecentPayments()
}