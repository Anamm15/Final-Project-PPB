package com.example.fitness.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Facility(
    val icon: ImageVector,
    val title: String,
    val desc: String
)

private val facilities = listOf(
    Facility(Icons.Filled.FitnessCenter, "Alat lengkap", "Free weight & mesin"),
    Facility(Icons.Filled.Groups, "Kelas grup", "Yoga, spinning, HIIT"),
    Facility(Icons.Filled.Person, "Personal trainer", "Bimbingan ahli"),
    Facility(Icons.Filled.MonitorHeart, "Area cardio", "Treadmill & sepeda"),
)

@Composable
fun HomeScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Daftar jadi member", fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Sudah punya akun?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = onLoginClick) {
                            Text("Masuk", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HeroSection()

            Spacer(Modifier.height(20.dp))
            Text(
                "Fasilitas kami",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(14.dp))

            // grid 2 kolom tanpa LazyGrid (aman di dalam Column yang scroll)
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                facilities.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { facility ->
                            FacilityCard(
                                facility = facility,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            RewardHighlight(modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 28.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.onPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "FitMember",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(18.dp))
            Text(
                "Wujudkan versi terbaik dirimu",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Fasilitas lengkap, latihan terarah, dan reward di setiap langkahmu.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun FacilityCard(facility: Facility, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    facility.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                facility.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                facility.desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RewardHighlight(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.CardGiftcard,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Kumpulkan poin, tukar reward",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Tiap sesi latihan menambah poinmu.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}