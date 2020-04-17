package com.example.smarthome

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_get_data.*
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.time.ExperimentalTime
import java.time.format.DateTimeFormatter

class GetDataActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_data)

        val actionbar = supportActionBar
        actionbar!!.title = "Latest Data"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val current = LocalDateTime.now()

        val date1 = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dated = current.format(date1)
        val hour1 =  DateTimeFormatter.ofPattern("HH")
        val houred = current.format(hour1)
        val time1 =  DateTimeFormatter.ofPattern("mmss")
        val timed = current.format(time1)

        val path1 = "PI_01_"+dated
        //path 2 is hour
        val path3 = timed.substring(0,3) + "0"
        datetimeView.text = path1+" "+houred+" "+path3

        getData(path1,houred,path3)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getData(path1:String,path2:String,path3:String){
        var ref = FirebaseDatabase.getInstance().getReference(path1).child(path2).child(path3)

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val piData = dataSnapshot.getValue(PiData::class.java)
                if(piData != null){
                    humidView.text = piData.humid
                    lightView.text = piData.light
                    soundView.text = piData.sound
                    tempeView.text = piData.tempe
                    ultraView.text = piData.ultra
                }
                else{
                    humidView.text = "null"
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        ref.addListenerForSingleValueEvent(menuListener)
    }
}
