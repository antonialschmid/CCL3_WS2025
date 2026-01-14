package com.example.gratitudegarden.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratitudegarden.data.model.GratitudeEntry
import com.example.gratitudegarden.data.repository.GratitudeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddEntryViewModel(
    private val repository: GratitudeRepository
) : ViewModel() {

    val entries = repository.getAllEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun saveEntry(
        text: String,
        mood: String,
        timestamp: Long
    ) {
        val entry = GratitudeEntry(
            id = 0,
            text = text,
            mood = mood,
            timestamp = timestamp
        )

        viewModelScope.launch {
            repository.insert(entry)
        }
    }

    fun updateEntry(entry: GratitudeEntry) {
        viewModelScope.launch {
            repository.update(entry)
        }
    }

    fun deleteEntry(entry: GratitudeEntry) {
        viewModelScope.launch {
            repository.delete(entry)
        }
    }

    fun getEntryById(id: Long): GratitudeEntry? {
        return entries.value.firstOrNull { it.id == id }
    }
}