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

    val entries = repository.getEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun saveEntry(text: String, mood: String) {
        val entry = GratitudeEntry(
            text = text,
            mood = mood,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.addEntry(entry)
        }
    }
    suspend fun getEntryById(id: Int): GratitudeEntry? {
        return repository.getEntryById(id)
    }

    fun updateEntry(entry: GratitudeEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    fun deleteEntry(entry: GratitudeEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }
}
