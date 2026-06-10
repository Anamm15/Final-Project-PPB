package com.example.fitness.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fitness.data.local.entity.UserEntity

@Entity(
    tableName = "memberships",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"], childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MembershipPackageEntity::class,
            parentColumns = ["id"], childColumns = ["packageId"]
        )
    ],
    indices = [Index("userId"), Index("packageId")]
)
data class MembershipEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val packageId: Long,
    val startDate: Long,
    val endDate: Long,
    val status: String = "ACTIVE" // "ACTIVE" | "EXPIRED" | "PENDING"
)