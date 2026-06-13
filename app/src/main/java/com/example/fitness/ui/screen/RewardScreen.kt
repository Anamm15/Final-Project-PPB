package com.example.fitness.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitness.data.local.entity.RewardEntity
import com.example.fitness.viewmodel.RewardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    onBack: () -> Unit,
    viewModel: RewardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tukar Reward") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            // Saldo poin
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Bolt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Poin kamu",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "%,d".format(state.pointBalance).replace(',', '.'),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state.rewards.isEmpty() && !state.isLoading) {
                Text(
                    "Belum ada reward tersedia.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.rewards, key = { it.id }) { reward ->
                        RewardCard(
                            reward = reward,
                            balance = state.pointBalance,
                            onRedeem = { viewModel.redeem(reward.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardCard(
    reward: RewardEntity,
    balance: Int,
    onRedeem: () -> Unit
) {
    val affordable = balance >= reward.pointCost
    val inStock = reward.stock > 0
    val canRedeem = affordable && inStock

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.CardGiftcard,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(reward.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(
                    "${reward.pointCost} poin · stok ${reward.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    when {
                        !inStock -> "Habis"
                        !affordable -> "Poin kurang"
                        else -> "Tukar"
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}