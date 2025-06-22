package com.denish3436.healthlog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.denish3436.healthlog.data.database.HealthEntry
import com.denish3436.healthlog.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    viewModel: HealthViewModel,
    onNavigateBack: () -> Unit
) {
    var waterIntake by remember { mutableStateOf("") }
    var sleepHours by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("Neutral") }
    var exerciseMinutes by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val moods = listOf("Happy", "Neutral", "Sad", "Excited", "Tired")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Add Health Entry",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Date
        Text(
            text = "Date: ${SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Water Intake
        OutlinedTextField(
            value = waterIntake,
            onValueChange = { waterIntake = it },
            label = { Text("Water Intake (glasses)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sleep Hours
        OutlinedTextField(
            value = sleepHours,
            onValueChange = { sleepHours = it },
            label = { Text("Sleep Hours") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mood Selection
        Text("Select Mood:", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            moods.forEach { mood ->
                FilterChip(
                    selected = selectedMood == mood,
                    onClick = { selectedMood = mood },
                    label = { Text(mood) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exercise Minutes
        OutlinedTextField(
            value = exerciseMinutes,
            onValueChange = { exerciseMinutes = it },
            label = { Text("Exercise (minutes)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Notes
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        Button(
            onClick = {
                val entry = HealthEntry(
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    waterIntake = waterIntake.toIntOrNull() ?: 0,
                    sleepHours = sleepHours.toFloatOrNull() ?: 0f,
                    mood = selectedMood,
                    exerciseMinutes = exerciseMinutes.toIntOrNull() ?: 0,
                    notes = notes
                )
                viewModel.addHealthEntry(entry)
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Entry")
        }
    }
}