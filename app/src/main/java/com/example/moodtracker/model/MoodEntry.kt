package com.example.moodtracker.model

data class MoodEntry(
    val id: String,
    val mood: String,
    val note: String,
    val timestamp: String, // ISO format "YYYY-MM-DD"
    val tags: List<String> = emptyList() // optional tags
)
