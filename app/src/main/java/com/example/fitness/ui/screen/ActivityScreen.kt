package com.example.fitness.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
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
import com.example.fitness.util.ActivityType
import com.example.fitness.util.formatDuration
import com.example.fitness.viewmodel.ActivityViewModel
import com.example.fitness.viewmodel.SessionPhase

private fun iconFor(type: ActivityType): ImageVector = when (type) {
    ActivityType.WEIGHT -> Icons.Filled.FitnessCenter
    ActivityType.CARDIO -> Icons.Filled.DirectionsRun
    ActivityType.YOGA -> Icons.Filled.SelfImprovement
    ActivityType.HIIT -> Icons.Filled.Bolt
}

@Composable
fun ActivityScreen(
    onDone: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            when (state.phase) {
                SessionPhase.IDLE -> "Mulai aktivitas"
                SessionPhase.RUNNING -> "Sesi latihan"
                SessionPhase.FINISHED -> "Sesi selesai"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(20.dp))

        when (state.phase) {
            SessionPhase.IDLE -> IdleContent(
                types = viewModel.activityTypes,
                selected = state.selectedType,
                onSelect = viewModel::onSelectType,
                onStart = viewModel::start
            )
            SessionPhase.RUNNING -> RunningContent(
                typeLabel = state.selectedType.label,
                icon = iconFor(state.selectedType),
                elapsed = state.elapsedSeconds,
                livePoints = state.livePoints,
                onFinish = viewModel::finish,
                onCancel = viewModel::cancel
            )
            SessionPhase.FINISHED -> FinishedContent(
                points = state.earnedPoints,
                onDone = onDone
            )
        }
    }
}

@Composable
private fun ColumnScope.IdleContent(
    types: List<ActivityType>,
    selected: ActivityType,
    onSelect: (ActivityType) -> Unit,
    onStart: () -> Unit
) {
    Text("Pilih jenis latihan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
    Spacer(Modifier.height(12.dp))

    types.chunked(2).forEach { row ->
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            row.forEach { type ->
                TypeCard(
                    icon = iconFor(type),
                    label = type.label,
                    hint = "${type.pointsPerMinute} poin/menit",
                    selected = type == selected,
                    onClick = { onSelect(type) },
                    modifier = Modifier.weight(1f)
                )
            }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
    }

    Spacer(Modifier.weight(1f))
    Button(
        onClick = onStart,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Mulai sesi", fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TypeCard(
    icon: ImageVector,
    label: String,
    hint: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            width = if (selected) 2.dp else 0.5.dp,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(
                Modifier.size(38.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(21.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ColumnScope.RunningContent(
    typeLabel: String,
    icon: ImageVector,
    elapsed: Long,
    livePoints: Int,
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    Spacer(Modifier.weight(1f))

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(typeLabel, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }

    Spacer(Modifier.height(28.dp))

    Box(
        Modifier.size(220.dp)
            .border(7.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .align(Alignment.CenterHorizontally),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(formatDuration(elapsed), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Medium)
            Text("durasi sesi", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

    Spacer(Modifier.height(20.dp))
    Text(
        "+$livePoints poin sejauh ini",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )

    Spacer(Modifier.weight(1f))
    Button(
        onClick = onFinish,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Selesai", fontWeight = FontWeight.Medium)
    }
    Spacer(Modifier.height(4.dp))
    TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.CenterHorizontally)) {
        Text("Batalkan sesi", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ColumnScope.FinishedContent(points: Int, onDone: () -> Unit) {
    Spacer(Modifier.weight(1f))
    Icon(
        Icons.Filled.CheckCircle,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(72.dp).align(Alignment.CenterHorizontally)
    )
    Spacer(Modifier.height(16.dp))
    Text(
        "Kerja bagus!",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
    Spacer(Modifier.height(6.dp))
    Text(
        "Kamu mendapatkan +$points poin",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
    Spacer(Modifier.weight(1f))
    Button(
        onClick = onDone,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Kembali ke dashboard", fontWeight = FontWeight.Medium)
    }
}