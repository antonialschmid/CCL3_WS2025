package com.example.gratitudegarden.data.repository

import com.example.gratitudegarden.db.GratitudeDao
import com.example.gratitudegarden.data.model.GratitudeEntry

class GratitudeRepository(
    private val dao: GratitudeDao
) {

    suspend fun addEntry(entry: GratitudeEntry) {
        dao.insertEntry(entry)
    }

    fun getEntries() = dao.getAllEntries()
}
