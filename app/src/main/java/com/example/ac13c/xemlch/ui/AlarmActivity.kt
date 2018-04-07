package com.example.ac13c.xemlch.ui

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.example.ac13c.xemlch.R
import com.example.ac13c.xemlch.local.Alarm
import com.example.ac13c.xemlch.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_alarm_screen.*

/**
 * @author by haolek on 31/03/2018.
 */
class AlarmActivity : AppCompatActivity() {

    companion object {
        private val WAKELOCK_TIME = 6 * 1000
        private val TAG = "AlarmActivity"
    }

    private lateinit var media: MediaPlayer
    private lateinit var wakelock: PowerManager.WakeLock
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_screen)
        val title = intent.getStringExtra(AlarmReceiver.TITLE)
        val content = intent.getStringExtra(AlarmReceiver.CONTENT)
        val hour = intent.getIntExtra(AlarmReceiver.HOUR, 0)
        val minute = intent.getIntExtra(AlarmReceiver.MINUTE, 0)
        val tone = intent.getStringExtra(AlarmReceiver.TONE)
        tvAlarmTitle.text = title
        tvAlarmTime.text = "${hour} : ${minute}"
        tvAlarmNote.text = content

        btnAlarmDismiss.setOnClickListener { v ->
            media.stop()
            finish()
        }

        media = MediaPlayer()
        if (!tone.isEmpty()) {
            media.setDataSource(this, Uri.parse(tone))
            media.setAudioStreamType(AudioManager.STREAM_RING)
            media.isLooping = true
            media.prepare()
            media.start()
        }

        val releaseWakelock = Runnable {
            // TODO Auto-generated method stub
            window.clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

            if (wakelock.isHeld()) {
                wakelock.release()
            }
        }

        Handler().postDelayed(releaseWakelock, WAKELOCK_TIME.toLong())
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

        val pm = application.getSystemService(Context.POWER_SERVICE) as PowerManager

        wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG)

        if (wakelock.isHeld()) {
            wakelock.acquire(1 * 60 * 1000L /*10 minutes*/)
        }
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()

        if (wakelock.isHeld()) {
            wakelock.release()
        }
    }
}