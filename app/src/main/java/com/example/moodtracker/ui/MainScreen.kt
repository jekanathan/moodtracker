package com.example.moodtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moodtracker.model.MoodEntry
import com.example.moodtracker.viewmodel.MoodViewModel

// Mood colors & emojis
private val moodColors = mapOf(
    "Happy" to Color(0xFFFFC7C7),
    "Calm" to Color(0xFFB2F4FF),
    "Neutral" to Color(0xFFD6E8FF),
    "Sad" to Color(0xFFCDE8CC),
    "Anxious" to Color(0xFFE6D5FF)
)
private val moodEmojis = mapOf(
    "Happy" to "😊",
    "Calm" to "🙂",
    "Neutral" to "😐",
    "Sad" to "😟",
    "Anxious" to "😬"
)

@Composable
fun MainScreen(navController: NavController, viewModel: MoodViewModel) {

    val moods = listOf(
        Triple("Happy", "😊", Color(0xFFFFC7C7)),
        Triple("Calm", "🙂", Color(0xFFB2F4FF)),
        Triple("Neutral", "😐", Color(0xFFD6E8FF)),
        Triple("Sad", "😟", Color(0xFFCDE8CC)),
        Triple("Anxious", "😬", Color(0xFFE6D5FF))
    )

    val entries = viewModel.moodEntries
    var selectedColor by remember { mutableStateOf(Color(0xFFE3F2FD)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(selectedColor)
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "How Are You Feeling?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(Modifier.height(16.dp))

        // Color selection
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            moodColors.values.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { selectedColor = color }
                )
                Spacer(Modifier.width(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        // Mood selection
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center) {
                    moods.take(3).forEach { item ->
                        MoodCircle(item.first, item.second, item.third) {
                            viewModel.addMood(
                                MoodEntry(
                                    id = System.currentTimeMillis().toString(),
                                    mood = item.first,
                                    note = "",
                                    timestamp = viewModel.getToday()
                                )
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    moods.takeLast(2).forEach { item ->
                        MoodCircle(item.first, item.second, item.third) {
                            viewModel.addMood(
                                MoodEntry(
                                    id = System.currentTimeMillis().toString(),
                                    mood = item.first,
                                    note = "",
                                    timestamp = viewModel.getToday()
                                )
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            Text(
                "Mood Details",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(10.dp))

            // Mood history list or empty message
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (entries.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No mood recorded yet. Please add a mood first.",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(entries) { entry ->
                        MoodHistoryCard(entry, navController) {
                            viewModel.deleteMood(entry.id)
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MoodCircle(label: String, emoji: String, bg: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(bg)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, style = MaterialTheme.typography.headlineLarge)
        }
        Spacer(Modifier.height(5.dp))
        Text(label)
    }
}

@Composable
fun MoodHistoryCard(entry: MoodEntry, nav: NavController, onDelete: () -> Unit) {
    val color = moodColors[entry.mood] ?: Color(0xFFEFEFEF)
    val emoji = moodEmojis[entry.mood] ?: "🙂"

    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { nav.navigate("details/${entry.id}") }
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${entry.mood} $emoji", fontWeight = FontWeight.Bold)
                Text(entry.timestamp, color = Color.DarkGray)
                if (entry.tags.isNotEmpty())
                    Text("Triggers: ${entry.tags.joinToString(", ")}", color = Color.Gray)
            }

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text("Delete", color = Color.White)
            }
        }
    }
}
