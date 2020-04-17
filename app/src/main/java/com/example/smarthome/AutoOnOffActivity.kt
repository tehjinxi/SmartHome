package com.example.smarthome

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_auto_on_off.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

class AutoOnOffActivity : AppCompatActivity()  {

    var light = ""
    var sound = ""
    var ultra = ""
    var auto = "False"
    var onOff = 0

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_on_off)

        val actionbar = supportActionBar
        actionbar!!.title = "Auto On Off"
        actionbar.setDisplayHomeAsUpEnabled(true)

        getData()

        if(auto == "True"){
            btnSetAuto.setBackgroundColor(Color.GREEN)
            btnSetAuto.setTextColor(Color.BLACK)
        }else{
            btnSetAuto.setBackgroundColor(Color.GRAY)
            btnSetAuto.setTextColor(Color.BLACK)
        }

        btnSetAuto.setOnClickListener{
            if(auto == "True"){
                auto = "False"
                btnSetAuto.setBackgroundColor(Color.GRAY)
                btnSetAuto.setTextColor(Color.BLACK)
            }else{
                auto = "True"
                btnSetAuto.setBackgroundColor(Color.GREEN)
                btnSetAuto.setTextColor(Color.BLACK)
            }

            check()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun getData(){
        val current = LocalDateTime.now()

        val date1 = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dated = "PI_01_"+current.format(date1)
        val hour1 =  DateTimeFormatter.ofPattern("HH")
        val houred = current.format(hour1)
        val time1 = DateTimeFormatter.ofPattern("mmss")
        val timed = (current.format(time1)).substring(0,3) + "0"

        var ref = FirebaseDatabase.getInstance().getReference(dated).child(houred).child(timed)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                light = p0.child("light").getValue().toString()
                sound = p0.child("sound").getValue().toString()
                ultra = p0.child("ultra").getValue().toString()
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun check(){
        getData()

        if(sound < "250"){
            if(ultra > "0"){
                light = "0"
            }
        }else{
            if(light == "0"){
                light = "1"
            }else{
                light = "0"
            }
        }

        val database = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")
        database.child("lcd").setValue(light)

        if(auto == "True"){
            refresh(1000)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    private fun refresh(milliseconds:Long){
        Handler().postDelayed({
            check()
        }, milliseconds)
    }


}