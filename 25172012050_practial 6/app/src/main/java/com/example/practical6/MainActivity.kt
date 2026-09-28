package com.example.practical61

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var alarmAnimation: AnimationDrawable
    private lateinit var heartAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ivAlarm = findViewById<ImageView>(R.id.ivAlarmAnimation)
        ivAlarm.setImageResource(R.drawable.animation)
        alarmAnimation = ivAlarm.drawable as AnimationDrawable

        val ivHeart = findViewById<ImageView>(R.id.ivHeart)
        ivHeart.setImageResource(R.drawable.heart_fill_animation)
        heartAnimation = ivHeart.drawable as AnimationDrawable

        findViewById<MaterialButton>(R.id.btnCreateAlarm).setOnClickListener {
            setAlarm()
        }

        findViewById<MaterialButton>(R.id.btnCancelAlarm).setOnClickListener {
            cancelAlarm()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            alarmAnimation.start()
            heartAnimation.start()
        }
    }

    private fun setAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10) // Set alarm for 10 seconds from now

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Toast.makeText(this, "Alarm set for 10 seconds", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            // Handle the case where SCHEDULE_EXACT_ALARM is not granted
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Toast.makeText(this, "Alarm set (inexact due to permission)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show()
    }
}