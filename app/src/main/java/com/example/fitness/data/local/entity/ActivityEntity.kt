package com.example.fitness.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fitness.data.local.entity.UserEntity

@Entity(
    tableName = "activities",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"], childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val type: String,               // "Cardio", "Angkat beban", dst
    val startTime: Long,
    val endTime: Long? = null,
    val pointsEarned: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)