package com.example.fitness.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "membership_packages")
data class MembershipPackageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,              // "Bulanan", "Tahunan"
    val durationDays: Int,         // 30, 365
    val price: Double,
    val description: String? = null,
    val isActive: Boolean = true
)