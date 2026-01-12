package com.example.gratitudegarden.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratitudegarden.data.model.GratitudeEntry
import com.example.gratitudegarden.data.repository.GratitudeRepository
import kotlinx.coroutines.launch

class AddEntryViewModel(
    private val repository: GratitudeRepository
) : ViewModel() {

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
}
