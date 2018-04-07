package com.example.ac13c.xemlch.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.ac13c.xemlch.R
import com.example.ac13c.xemlch.local.Alarm
import com.example.ac13c.xemlch.local.EventDatabase
import com.example.ac13c.xemlch.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_deltail_event.*
import java.util.*

@SuppressLint("SetTextI18n")
class DetailEventActivity : AppCompatActivity() {
    companion object {
        val NEW_KEY = "new_key"
        val EVENT_KEY = "event_key"
        val DETAIL_KEY = "detail_key"
    }

    private var eventEntity = Alarm()
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0

    private var key: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deltail_event)

        key = intent.getStringExtra(NEW_KEY)
        if (key == "detailKey") {
            eventEntity = intent.getBundleExtra(EVENT_KEY).getSerializable(DETAIL_KEY) as Alarm
            edtTitle.setText(eventEntity.title)
            edtAddress.setText(eventEntity.address)
            edtContent.setText(eventEntity.content)
            this.day = eventEntity.day
            this.month = eventEntity.month
            this.year = eventEntity.year
            this.minute = eventEntity.minute
            this.hour = eventEntity.hour
            tvDay.text = "${day} / ${month} / ${year}"
            tvTime.text = "${hour} : ${minute}"
            tvAlarmToneSelection.text = RingtoneManager.getRingtone(this, Uri.parse(eventEntity.tone)).getTitle(this)
        } else {
            val intent = intent
            tvDay.text = intent.getStringExtra("date")
        }

        tvSave.setOnClickListener({ view ->
            saveData()
            AlarmReceiver.setAlarmReceiver(this)
            finish()
        })

        btnDay.setOnClickListener { v ->
            funDate()
        }

        btnTime.setOnClickListener { v ->
            funTime()
        }

        llAlarmTone.setOnClickListener { v ->
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            startActivityForResult(intent, 1)
        }
    }

    fun saveData() {
        Toast.makeText(this@DetailEventActivity, "Save", Toast.LENGTH_SHORT).show()
        eventEntity.title = edtTitle.text.toString()
        eventEntity.address = edtAddress.text.toString()
        eventEntity.content = edtContent.text.toString()
        eventEntity.day = day
        eventEntity.month = month
        eventEntity.year = year
        eventEntity.hour = hour
        eventEntity.minute = minute
        if (key == "detailKey") {
            EventDatabase.getInstance(this)?.eventDao()?.updateEvent(eventEntity)
        } else {
            EventDatabase.getInstance(this)?.eventDao()?.insertEvent(eventEntity)
        }
    }

    fun funDate() {
        val c = Calendar.getInstance()
        val dayCurrent = c.get(Calendar.DAY_OF_MONTH)
        val monthCurrent = c.get(Calendar.MONTH)
        val yearCurrent = c.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Panel,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    tvDay.text = "$day / ${month + 1} / $year"
                    this.day = day
                    this.month = month + 1
                    this.year = year
                }, yearCurrent, monthCurrent, dayCurrent)

        datePickerDialog.show()
    }

    fun funTime() {
        val cal = Calendar.getInstance()
        val hourCurrent = cal.get(Calendar.HOUR_OF_DAY)
        val minuteCurrent = cal.get(Calendar.MINUTE)
        val tpd = TimePickerDialog(this, android.R.style.Theme_Holo_Light_Panel,
                TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                    tvTime.text = "${hour}:${min}"
                    this.hour = hour
                    this.minute = min
                }, hourCurrent, minuteCurrent, true)

        tpd.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    eventEntity.tone = data.getParcelableExtra<Parcelable>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString()
                    tvAlarmToneSelection.setText(RingtoneManager.getRingtone(this, Uri.parse(eventEntity.tone)).getTitle(this))
                }
                else -> {
                }
            }
        }
    }
}
