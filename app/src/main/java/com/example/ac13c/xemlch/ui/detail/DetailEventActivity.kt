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
    private var mDay: Int = 0
    private var mMonth: Int = 0
    private var mYear: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0

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
            this.mDay = eventEntity.day
            this.mMonth = eventEntity.month
            this.mYear = eventEntity.year
            this.mMinute = eventEntity.minute
            this.mHour = eventEntity.hour
            tvDay.text = "$mDay / $mMonth / $mYear"
            tvTime.text = "$mHour : $mMinute"
            tvAlarmToneSelection.text = RingtoneManager.getRingtone(this, Uri.parse(eventEntity.tone)).getTitle(this)
        } else {
            val intent = intent
            tvDay.text = intent.getStringExtra("date")
        }

        imgBack.setOnClickListener { onBackPressed() }

        tvSave.setOnClickListener({
            saveData()
            AlarmReceiver.setAlarmReceiver(this)
            finish()
        })

        tvDay.setOnClickListener {
            funDate()
        }

        tvTime.setOnClickListener {
            funTime()
        }

        llAlarmTone.setOnClickListener { v ->
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            startActivityForResult(intent, 1)
        }
    }

    private fun saveData() {
        Toast.makeText(this@DetailEventActivity, "Save", Toast.LENGTH_SHORT).show()
        eventEntity.run {
            title = edtTitle.text.toString()
            address = edtAddress.text.toString()
            content = edtContent.text.toString()
            day = mDay
            month = mMonth
            year = mYear
            hour = mHour
            minute = mMinute
        }

        if (key == "detailKey") {
            EventDatabase.getInstance(this)?.eventDao()?.updateEvent(eventEntity)
        } else {
            EventDatabase.getInstance(this)?.eventDao()?.insertEvent(eventEntity)
        }
    }

    private fun funDate() {
        val c = Calendar.getInstance()
        val dayCurrent = c.get(Calendar.DAY_OF_MONTH)
        val monthCurrent = c.get(Calendar.MONTH)
        val yearCurrent = c.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Panel,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    tvDay.text = "$day / ${month + 1} / $year"
                    this.mDay = day
                    this.mMonth = month + 1
                    this.mYear = year
                }, yearCurrent, monthCurrent, dayCurrent)

        datePickerDialog.show()
    }

    private fun funTime() {
        val cal = Calendar.getInstance()
        val hourCurrent = cal.get(Calendar.HOUR_OF_DAY)
        val minuteCurrent = cal.get(Calendar.MINUTE)
        val tpd = TimePickerDialog(this, android.R.style.Theme_Holo_Light_Panel,
                TimePickerDialog.OnTimeSetListener { _, hour, min ->
                    tvTime.text = "${hour}:${min}"
                    this.mHour = hour
                    this.mMinute = min
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
