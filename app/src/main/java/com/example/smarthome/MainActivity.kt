package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lightbtn.setOnClickListener{
            controlLight("light")
        }
        buzzerbtn.setOnClickListener{
            controlLight("buzzer")
        }
        ledbtn.setOnClickListener{
            controlLight("led")
        }
        camerabtn.setOnClickListener{
            controlLight("camera")
        }
        lcdbtn.setOnClickListener{
            controlLight("lcd")
        }
        lcdtextbtn.setOnClickListener{
            controlLight("lcdtext")
        }
        relaybtn.setOnClickListener{
            controlLight("relay")
        }
    }

    private fun controlLight(sensor: String){

        val database = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")
        database.child(sensor).setValue("1")
            .addOnCompleteListener{
                Toast.makeText(this, "Switch on the light", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to Switch on the light", Toast.LENGTH_SHORT).show()
            }
    }
}
