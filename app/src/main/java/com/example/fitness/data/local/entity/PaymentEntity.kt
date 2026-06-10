package com.example.fitness.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fitness.data.local.entity.UserEntity
import com.example.fitness.data.local.entity.MembershipEntity

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"], childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MembershipEntity::class,
            parentColumns = ["id"], childColumns = ["membershipId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("userId"), Index("membershipId")]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val membershipId: Long? = null,
    val amount: Double,
    val method: String,             // "TRANSFER" | "CARD" | "EWALLET"
    val status: String = "PENDING", // "PENDING" | "SUCCESS" | "FAILED"
    val createdAt: Long = System.currentTimeMillis()
)