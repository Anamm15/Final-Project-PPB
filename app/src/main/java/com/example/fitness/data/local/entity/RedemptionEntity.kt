package com.example.fitness.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fitness.data.local.entity.UserEntity

@Entity(
    tableName = "redemptions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"], childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RewardEntity::class,
            parentColumns = ["id"], childColumns = ["rewardId"]
        )
    ],
    indices = [Index("userId"), Index("rewardId")]
)
data class RedemptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val rewardId: Long,
    val pointsSpent: Int,
    val status: String = "COMPLETED", // "COMPLETED" | "CANCELLED"
    val createdAt: Long = System.currentTimeMillis()
)