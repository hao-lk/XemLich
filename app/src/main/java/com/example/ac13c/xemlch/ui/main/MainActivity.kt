package com.example.ac13c.xemlch.ui.main

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.ac13c.xemlch.R
import com.example.ac13c.xemlch.local.Alarm
import com.example.ac13c.xemlch.local.EventDatabase
import com.example.ac13c.xemlch.receiver.AlarmReceiver
import com.example.ac13c.xemlch.ui.detail.DetailEventActivity
import com.example.ac13c.xemlch.ui.detail.DetailEventActivity.Companion.EVENT_KEY
import com.example.ac13c.xemlch.ui.detail.DetailEventActivity.Companion.NEW_KEY
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), CalendarEventAdapter.OnItemClick {
    private var date: String = ""
    private lateinit var eventViewModel: EventViewModel
    private lateinit var adapter: CalendarEventAdapter
    private var listDataEvent: List<Alarm>? = null
    private var eventEntity = Alarm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AlarmReceiver.setAlarmReceiver(this)

        calendar.setOnDateChangeListener { calendarView, year, month, day ->
            date = "$day / ${month + 1} / $year"
        }

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        adapter = CalendarEventAdapter(ArrayList())
        recyclerView.adapter = adapter
        adapter.clickItem(this)

        eventViewModel.eventEntities?.observe(this, Observer { eventEntities ->
            adapter.setList(eventEntities!!)
        })
        btnSelect.setOnClickListener {
            val intent = Intent(this@MainActivity, DetailEventActivity::class.java)
            intent.putExtra(NEW_KEY, "newKey")
            intent.putExtra("date", date)
            startActivityForResult(intent, 1001)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            AlarmReceiver.setAlarmReceiver(this)
        }
    }

    override fun onImage(alarm: Alarm) {
        super.onImage(alarm)
        EventDatabase.getInstance(this)!!.eventDao().deleteEvent(alarm)
        Toast.makeText(this@MainActivity, "Delete", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClickDetail(alarm: Alarm) {
        super.onItemClickDetail(alarm)
        val intent = Intent(this@MainActivity, DetailEventActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(DetailEventActivity.DETAIL_KEY, alarm)
        intent.putExtra(EVENT_KEY, bundle)
        intent.putExtra(NEW_KEY, "detailKey")
        startActivity(intent)
    }

}
