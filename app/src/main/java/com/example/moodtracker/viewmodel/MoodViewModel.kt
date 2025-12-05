package com.example.moodtracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.moodtracker.model.MoodEntry
import java.text.SimpleDateFormat
import java.util.*

class MoodViewModel : ViewModel() {

    val moodEntries = mutableStateListOf<MoodEntry>()

    fun addMood(entry: MoodEntry) {
        val existingEntry = moodEntries.find { it.timestamp == entry.timestamp && it.mood == entry.mood }
        if (existingEntry == null) {
            moodEntries.add(0, entry)
        }
    }

    fun deleteMood(id: String) {
        moodEntries.removeAll { it.id == id }
    }

    fun getMoodEntry(id: String): MoodEntry? {
        return moodEntries.find { it.id == id }
    }

    fun updateMoodNote(id: String, newNote: String) {
        val index = moodEntries.indexOfFirst { it.id == id }
        if (index != -1) {
            val entry = moodEntries[index]
            moodEntries[index] = entry.copy(note = newNote)
        }
    }

    fun getToday(): String {
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    fun getCurrentWeekMoods(): List<List<String>> {
        val calendar = Calendar.getInstance()
        val last7Days = mutableListOf<List<String>>()
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())

        for (i in 0 until 7) {
            val date = formatter.format(calendar.time)
            val moodsForDay = moodEntries.filter { it.timestamp == date }.map { it.mood }
            last7Days.add(moodsForDay)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return last7Days.reversed()
    }

    fun getLast7DaysLabels(): List<String> {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("EEE", Locale.getDefault())
        return List(7) {
            val label = formatter.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            label
        }.reversed()
    }
}
