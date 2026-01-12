package com.example.gratitudegarden.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gratitudegarden.data.model.GratitudeEntry

@Database(
    entities = [GratitudeEntry::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gratitudeDao(): GratitudeDao
}
