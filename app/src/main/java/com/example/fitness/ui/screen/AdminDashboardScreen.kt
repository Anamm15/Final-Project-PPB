package com.example.fitness.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitness.data.local.dao.PaymentWithUser
import com.example.fitness.viewmodel.AdminDashboardViewModel
import com.example.fitness.viewmodel.formatDate
import com.example.fitness.viewmodel.formatRupiah

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val stats = state.stats

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Dashboard Admin",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Pemantauan FitMember",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onLogout) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Keluar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        RevenueCard(
            revenue = stats.totalRevenue,
            paymentCount = stats.successfulPayments
        )

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(Icons.Filled.Group, "Total member", stats.totalMembers.toString(), Modifier.weight(1f))
            StatCard(Icons.Filled.Verified, "Membership aktif", stats.activeMemberships.toString(), Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(Icons.Filled.FitnessCenter, "Aktivitas tercatat", stats.totalActivities.toString(), Modifier.weight(1f))
            StatCard(Icons.Filled.CardGiftcard, "Reward ditukar", stats.totalRedemptions.toString(), Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Transaksi terakhir",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(12.dp))

        if (state.recentPayments.isEmpty()) {
            Text(
                "Belum ada transaksi.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            state.recentPayments.forEach { payment ->
                PaymentRow(payment)
                Spacer(Modifier.height(14.dp))
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun RevenueCard(revenue: Double, paymentCount: Int) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    "Total pendapatan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
                Text(
                    formatRupiah(revenue.toLong()),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    "dari $paymentCount transaksi sukses",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
            Box(
                Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Paid,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun StatCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PaymentRow(payment: PaymentWithUser) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(38.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                payment.userName.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(payment.userName, style = MaterialTheme.typography.bodyLarge)
            Text(
                "${methodLabel(payment.method)} · ${formatDate(payment.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                formatRupiah(payment.amount.toLong()),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                statusLabel(payment.status),
                style = MaterialTheme.typography.labelSmall,
                color = if (payment.status == "SUCCESS") MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
        }
    }
}

private fun methodLabel(method: String) = when (method) {
    "TRANSFER" -> "Transfer bank"
    "EWALLET" -> "E-wallet"
    "CARD" -> "Kartu"
    else -> method
}

private fun statusLabel(status: String) = when (status) {
    "SUCCESS" -> "Sukses"
    "PENDING" -> "Menunggu"
    "FAILED" -> "Gagal"
    else -> status
}