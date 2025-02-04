package com.example.smarthome

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_auto_on_off.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

class AutoOnOffActivity : AppCompatActivity() {

    private var light = ""
    private var sound = ""
    private var ultra = ""
    private var auto = "False"
    private var defaultSoundLimit = "200"
    private var soundLimit = ""

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_on_off)

        val actionbar = supportActionBar
        actionbar!!.title = "Auto On Off"
        actionbar.setDisplayHomeAsUpEnabled(true)

        getData()

        if (soundLimit == "") {
            txtSoundLimit.setText(defaultSoundLimit)
            soundLimit = defaultSoundLimit
        }

        if (auto == "True") {
            btnSetAuto.setBackgroundColor(Color.GREEN)
            btnSetAuto.setTextColor(Color.BLACK)
        } else {
            btnSetAuto.setBackgroundColor(Color.GRAY)
            btnSetAuto.setTextColor(Color.BLACK)
        }

        btnSetLimit.setOnClickListener {
            soundLimit = txtSoundLimit.text.toString()
            Toast.makeText(this, "Limit has been set.", Toast.LENGTH_LONG).show()
        }

        btnSetAuto.setOnClickListener {
            Toast.makeText(this, "The sound limit.is $soundLimit", Toast.LENGTH_LONG).show()
            if (auto == "True") {
                auto = "False"
                btnSetAuto.setBackgroundColor(Color.GRAY)
                btnSetAuto.setTextColor(Color.BLACK)
                mHandler.removeCallbacks(mRunnable)
                btnSetLimit.isClickable = true
                btnSetLimit.setBackgroundColor(Color.GRAY)
            } else {
                auto = "True"
                btnSetAuto.setBackgroundColor(Color.GREEN)
                btnSetAuto.setTextColor(Color.BLACK)
                btnSetLimit.isClickable = false
                btnSetLimit.setBackgroundColor(Color.DKGRAY)

                if (soundLimit == "") {
                    soundLimit = defaultSoundLimit
                }

                mHandler = Handler()
                mRunnable = Runnable {
                    check()
                    mHandler.postDelayed(mRunnable, 5000)
                }
                mHandler.postDelayed(mRunnable, 5000)
            }
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun getData() {
        val current = LocalDateTime.now()

        val date1 = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dated = "PI_01_" + current.format(date1)
        val hour1 = DateTimeFormatter.ofPattern("HH")
        val houred = current.format(hour1)
        val time1 = DateTimeFormatter.ofPattern("mmss")
        val timed = (current.format(time1)).substring(0, 3) + "0"

        var ref = FirebaseDatabase.getInstance().getReference(dated).child(houred).child(timed)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("light").value != null && p0.child("sound").value != null && p0.child("ultra").value != null) {
                    light = p0.child("light").getValue().toString()
                    sound = p0.child("sound").getValue().toString()
                    ultra = p0.child("ultra").getValue().toString()
                }


            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val database = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")
        database.child("buzzer").setValue("1")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun check() {
        getData()
        Toast.makeText(this, "sound:$sound ultra:$ultra", Toast.LENGTH_LONG).show()
        if (sound != "" && ultra != "" && light != "") {
            if (sound.toInt() < soundLimit.toInt()) {
                if (ultra.toInt() > 0) {
                    if (light == "1") {
                        light = "0"
                    } else if (light == "0") {
                        light = "1"
                    }
                }
            }
        } else {
            light = "1"
        }

        val database = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")
        database.child("led").setValue(light)
    }
}