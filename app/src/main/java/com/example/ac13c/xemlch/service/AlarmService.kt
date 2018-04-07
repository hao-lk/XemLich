package com.example.ac13c.xemlch.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.ac13c.xemlch.ui.alarm.AlarmActivity

/**
 * @author by haolek on 31/03/2018.
 */
class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmIntent = Intent(baseContext, AlarmActivity::class.java)
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        alarmIntent.putExtras(intent)

        application.startActivity(alarmIntent)
        return super.onStartCommand(intent, flags, startId)

//        AlarmReceiver.setAlarmReceiver(this)
//        return START_STICKY
    }
}