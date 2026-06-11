package com.example.fitness.viewmodel

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MembershipTier(
    val id: Long,
    val name: String,
    val durationLabel: String,
    val durationDays: Int,
    val price: Long,
    val perk: String,
    val recommended: Boolean = false
)

val membershipTiers = listOf(
    MembershipTier(1, "Basic", "1 bulan", 30, 150_000, "Akses gym & area cardio"),
    MembershipTier(2, "Plus", "6 bulan", 180, 750_000, "+ kelas grup", recommended = true),
    MembershipTier(3, "Pro", "12 bulan", 365, 1_200_000, "+ personal trainer"),
)

fun formatRupiah(value: Long): String {
    val grouped = value.toString().reversed().chunked(3).joinToString(".").reversed()
    return "Rp$grouped"
}

fun formatDate(millis: Long): String =
    SimpleDateFormat("d MMM yyyy", Locale("id", "ID")).format(Date(millis))