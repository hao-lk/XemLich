package com.example.ac13c.xemlch.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

/**
 * Created by ac13c on 1/17/2018.
 */
@Dao
interface EventDao {
    @get:Query("SELECT * FROM calendar")
    val getAllCalendar: LiveData<List<Alarm>>

    @Query("SELECT * FROM calendar where day LIKE :day and month LIKE :month and year LIKE :year")
    fun getInfoAlarm(day: Int, month: Int, year: Int): Alarm

    @Insert
    fun insertEvent(calendar: Alarm)

    @Delete
    fun deleteEvent(alarm: Alarm)

    @Update
    fun updateEvent(calendar: Alarm)

}
