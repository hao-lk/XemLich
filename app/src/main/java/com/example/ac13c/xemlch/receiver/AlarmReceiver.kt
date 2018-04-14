package com.example.ac13c.xemlch.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ac13c.xemlch.local.Alarm
import com.example.ac13c.xemlch.local.EventDatabase
import com.example.ac13c.xemlch.service.AlarmService
import java.util.*

/**
 * @author by haolek on 31/03/2018.
 */
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val TITLE = "title"
        const val CONTENT = "content"
        const val HOUR = "hour"
        const val MINUTE = "minute"
        const val TONE = "tone"

        fun setAlarmReceiver(context: Context) {
            val calendar = Calendar.getInstance()
            val dayCurrent = calendar.get(Calendar.DAY_OF_MONTH)
            val monthCurrent = calendar.get(Calendar.MONTH) + 1
            val yearCurrent = calendar.get(Calendar.YEAR)

            val nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val nowMinute = Calendar.getInstance().get(Calendar.MINUTE)

            Log.e("hhhh", "$dayCurrent $monthCurrent $yearCurrent")

            val alarm = EventDatabase.getInstance(context)?.eventDao()?.getInfoAlarm(dayCurrent, monthCurrent, yearCurrent) as Alarm

            // Todo Xem sau
            if (alarm.hour > nowHour || ((alarm.hour == nowHour)) && (alarm.minute > nowMinute)) {
                val pendingIntent = createPendingIntent(context, alarm)
                val calendarAlarm = Calendar.getInstance()
                calendarAlarm.set(Calendar.DAY_OF_WEEK, alarm.day)
                calendarAlarm.set(Calendar.HOUR_OF_DAY, alarm.hour)
                calendarAlarm.set(Calendar.MINUTE, alarm.minute)
                calendarAlarm.set(Calendar.SECOND, 0)
                setAlarm(context, calendarAlarm, pendingIntent)
            }

        }

        private fun createPendingIntent(context: Context, alarm: Alarm): PendingIntent? {
            val intent = Intent(context, AlarmService::class.java)
            intent.putExtra(TITLE, alarm.title)
            intent.putExtra(CONTENT, alarm.content)
            intent.putExtra(HOUR, alarm.hour)
            intent.putExtra(MINUTE, alarm.minute)
            intent.putExtra(TONE, alarm.tone)
            return PendingIntent.getService(context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun setAlarm(context: Context, calendar: Calendar, pendingIntent: PendingIntent?) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.e("hhhh", "ALARM RECEIVER")
        setAlarmReceiver(context)
    }

}
