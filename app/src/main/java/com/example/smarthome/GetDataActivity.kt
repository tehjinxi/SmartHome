package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_get_data.*

class GetDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_data)

        val actionbar = supportActionBar
        actionbar!!.title = "Latest Data"
        actionbar.setDisplayHomeAsUpEnabled(true)

        getData()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getData(){
        //var PiData: PiData? = null  // declare user object outside onCreate Method

        var ref = FirebaseDatabase.getInstance().getReference("PI_01_20200417").child("00").child("0000")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val PiData = dataSnapshot.getValue()
                textView.text = PiData.toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        ref.addListenerForSingleValueEvent(menuListener)
    }
}
