package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_alert_avtivity.*
import kotlinx.android.synthetic.main.activity_get_data.*

class AlertAvtivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_avtivity)
        getImage()
    }

    private fun getImage(){
        var onOff = ""

        //firebase 2
        //FirebaseApp.initializeApp(this, options, "smart-home-iot-4b593")
        val secondApp = FirebaseApp.getInstance("smart-home-iot-4b593")
        val hi = FirebaseDatabase.getInstance(secondApp).getReference("TimeAlert")
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val piControl = dataSnapshot.getValue()
                if(piControl != null){
                    var second = piControl.toString().substring(12,13).toInt()
                    second += 1
                    var minute = piControl.toString().substring(10,12).toInt()
                    if(second == 6){
                        second = 0
                        minute += 1
                    }
                    val inMin = minute
                    val image1 = "cam_" + piControl.toString().substring(0,10) + minute + second + "0.jpg"
                    Picasso.get().load(image1).into(imageView)
                    second += 1
                    if(second == 6){
                        second = 0
                        minute += 1
                    }
                    val image2 = image1.substring(0,14) + minute + second + "0.jpg"
                    Picasso.get().load(image1).into(imageView)
                    second += 1
                    if(second == 6){
                        second = 0
                        minute += 1
                    }
                    val image3 = image1.substring(0,14) + minute + second + "0.jpg"
                    Picasso.get().load(image1).into(imageView)

                    downloadBtn.text = image1
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        hi.addListenerForSingleValueEvent(menuListener)
    }
}
