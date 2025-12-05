package com.example.moodtracker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moodtracker.viewmodel.MoodViewModel

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodDetailsScreen(
    navController: NavController,
    viewModel: MoodViewModel,
    entryId: String
) {
    val entry by remember { derivedStateOf { viewModel.getMoodEntry(entryId) } }
    var noteText by remember(entry) { mutableStateOf(entry?.note ?: "") }

    val weeklyMoods by remember(viewModel.moodEntries) {
        derivedStateOf { viewModel.getCurrentWeekMoods() }
    }
    val dayLabels = viewModel.getLast7DaysLabels()

    val motivationalQuotes = listOf(
        "Keep smiling, it suits you!",
        "Every day is a fresh start.",
        "Focus on the good things.",
        "You are stronger than you think."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(pad)
                .padding(20.dp)
        ) {

            if (entry != null) {
                val moodEmoji = moodEmojis[entry!!.mood] ?: ""

                Text(
                    "Mood: ${entry!!.mood} $moodEmoji",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text("Note:", style = MaterialTheme.typography.titleMedium)

                TextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Write your note here...") }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    "Date: ${entry!!.timestamp}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(10.dp))

                Button(onClick = { viewModel.updateMoodNote(entryId, noteText) }) {
                    Text("Save Note")
                }

                Spacer(Modifier.height(20.dp))
            }

            Text("Weekly Mood Overview", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFFFF), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                StackedWeeklyMoodChart(weeklyMoods, dayLabels)
            }

            Spacer(Modifier.height(20.dp))

            // Motivational Quote
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = motivationalQuotes.random(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun StackedWeeklyMoodChart(weeklyMoods: List<List<String>>, dayLabels: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weeklyMoods.forEachIndexed { dayIndex, dailyMoods ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                val totalHeight = 150.dp
                val totalMoods = dailyMoods.size.coerceAtLeast(1)

                moodColors.keys.forEach { mood ->
                    val count = dailyMoods.count { it == mood }
                    if (count > 0) {
                        val percent = count.toFloat() / totalMoods.toFloat()
                        val barHeight = totalHeight.value * percent
                        Canvas(
                            modifier = Modifier
                                .width(30.dp)
                                .height(barHeight.dp)
                        ) {
                            drawRoundRect(
                                color = moodColors[mood] ?: Color.Gray,
                                topLeft = Offset(0f, 0f),
                                cornerRadius = CornerRadius(12f, 12f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))
                Text(dayLabels[dayIndex], style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        moodColors.forEach { (mood, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(16.dp)) { drawRect(color = color) }
                Spacer(Modifier.width(4.dp))
                Text(mood, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
