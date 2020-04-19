package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_get_data.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val options = FirebaseOptions.Builder()
        .setApplicationId("1:297703301844:android:44861c0c2dc6782b489141")
        .setApiKey("AIzaSyA0uxfYFoVsJMSzYs_rhoNCsAYHMd2bEWo")
        .setDatabaseUrl("https://smart-home-iot-4b593.firebaseio.com")
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this, options, "smart-home-iot-4b593")


        buzzerbtn.setOnClickListener{
            getSwitchData("buzzer")
        }
        camerabtn.setOnClickListener{
            getSwitchData("camera")
        }
        lcdbtn.setOnClickListener{
            getSwitchData("lcd")
        }
        ledbtn.setOnClickListener{
            getSwitchData("led")
        }
        lightbtn.setOnClickListener{
            getSwitchData("light")
        }
        relaybtn.setOnClickListener{
            getSwitchData("relay")
        }
        onAlarmBtn.setOnClickListener {
            getData("alarmSys")
        }

        getDataBtn.setOnClickListener{
            //call activity
            val intent = Intent (this, GetDataActivity::class.java)
            startActivity(intent)
        }

        btnAuto.setOnClickListener{
            //call activity
            val intent = Intent (this, AutoOnOffActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getSwitchData(sensor: String){
        var switch = ""
        var ref = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL").child(sensor)
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val piControl = dataSnapshot.getValue()
                if(piControl != null){
                    if(piControl == "1"){
                        switch = "0"
                        control(sensor,switch)
                    }
                    else{
                        switch = "1"
                        control(sensor,switch)
                    }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        ref.addListenerForSingleValueEvent(menuListener)
    }

    private fun control(sensor: String, switch: String){
        val database = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")
        database.child(sensor).setValue(switch)
            .addOnCompleteListener{
                if(switch == "1"){
                    Toast.makeText(this, "Switch on the " + sensor, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Switch off the " + sensor, Toast.LENGTH_LONG).show()

                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to control", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getData(system: String){
        var onOff = ""

        //firebase 2
        //FirebaseApp.initializeApp(this, options, "smart-home-iot-4b593")
        val secondApp = FirebaseApp.getInstance("smart-home-iot-4b593")
        val hi = FirebaseDatabase.getInstance(secondApp).getReference(system)
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val piControl = dataSnapshot.getValue()
                if(piControl != null){
                    if(piControl == "1"){
                        onOff = "0"
                        controlSystem(system,onOff)
                    }
                    else{
                        onOff = "1"
                        controlSystem(system,onOff)
                    }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        hi.addListenerForSingleValueEvent(menuListener)
    }

    private fun controlSystem(system: String, onOff: String){
        //firebase 2
        //FirebaseApp.initializeApp(this, options, "smart-home-iot-4b593")
        val secondApp = FirebaseApp.getInstance("smart-home-iot-4b593")
        val hi = FirebaseDatabase.getInstance(secondApp).getReference(system).setValue(onOff)
            .addOnCompleteListener{
                if(onOff == "1"){
                    Toast.makeText(this, "Switch on the " + system, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Switch off the " + system, Toast.LENGTH_LONG).show()

                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to control", Toast.LENGTH_SHORT).show()
            }
    }
}
