package com.example.ac13c.xemlch.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.ac13c.xemlch.local.EventDatabase
import com.example.ac13c.xemlch.local.Alarm

/**
 * Created by ac13c on 1/15/2018.
 */
class EventViewModel(application: Application) : AndroidViewModel(application) {
    var eventEntities: LiveData<List<Alarm>>? = null
    private var eventDatabase: EventDatabase = EventDatabase.getInstance(this.getApplication())!!

    init {
        eventEntities = eventDatabase.eventDao().getAllCalendar
    }
}