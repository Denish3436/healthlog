package com.denish3436.healthlog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.denish3436.healthlog.data.database.HealthEntry
import com.denish3436.healthlog.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HealthViewModel,
    onNavigateToAdd: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val recentEntries by viewModel.recentEntries.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add Entry Button
        Button(
            onClick = onNavigateToAdd,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Health Entry")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Summary
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val todayEntry = recentEntries.find { it.date == today }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Today's Summary",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (todayEntry != null) {
                    Text("💧 Water: ${todayEntry.waterIntake} glasses")
                    Text("😴 Sleep: ${todayEntry.sleepHours} hours")
                    Text("😊 Mood: ${todayEntry.mood}")
                    Text("🏃 Exercise: ${todayEntry.exerciseMinutes} minutes")
                } else {
                    Text(
                        text = "No entry for today. Add one to get started!",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Entries
        Text(
            text = "Recent Entries",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recentEntries.take(5)) { entry ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = entry.date,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("💧 ${entry.waterIntake}")
                            Text("😴 ${entry.sleepHours}h")
                            Text("🏃 ${entry.exerciseMinutes}m")
                        }
                    }
                }
            }
        }

        // Error Snackbar
        uiState.error?.let { error ->
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(error, modifier = Modifier.weight(1f))
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}