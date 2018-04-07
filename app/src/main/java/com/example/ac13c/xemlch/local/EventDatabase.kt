package com.example.ac13c.xemlch.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by ac13c on 1/17/2018.
 */
@Database(entities = [(Alarm::class)], version = 1)
abstract class EventDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: EventDatabase? = null
        val DATABASE_NAME = "event-database"

        fun getInstance(context: Context): EventDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder<EventDatabase>(context.applicationContext,
                        EventDatabase::class.java,
                        DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE
        }
    }

    abstract fun eventDao(): EventDao
}