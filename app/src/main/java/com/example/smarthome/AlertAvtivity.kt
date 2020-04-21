package com.example.smarthome

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_alert_avtivity.*
import kotlinx.android.synthetic.main.activity_get_data.*
import java.io.File
import java.util.jar.Manifest

class AlertAvtivity : AppCompatActivity() {

    var imageUrl1 = ""
    var imageUrl2 = ""
    var imageUrl3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_avtivity)

        val actionbar = supportActionBar
        actionbar!!.title = "Alert image"
        actionbar.setDisplayHomeAsUpEnabled(true)

        getImage()
        downloadBtn.setOnClickListener {
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save in External Storage(Micro SD)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                askPermissions()
            } else {
                downloadImage(imageUrl1,imageUrl2,imageUrl3)
                //downloadImage(imageUrl2)
                //downloadImage(imageUrl3)
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
                    var image1 = ""
                    var image2 = ""
                    var image3 = ""

                    var second = piControl.toString().substring(12,13).toInt()
                    second += 1
                    var minute = piControl.toString().substring(10,12).toInt()
                    if(second == 6) {
                        second = 0
                        minute += 1
                    }
                    if (minute.toString().length == 1) {
                        image1 = "cam_" + piControl.toString().substring(0,10) + "0" + minute + second + "0.jpg"
                        Picasso.get().load(image1).into(imageView)
                    }
                    else{
                        image1 = "cam_" + piControl.toString().substring(0,10) + minute + second + "0.jpg"
                        Picasso.get().load(image1).into(imageView)
                    }

                    second += 1
                    if(second == 6){
                        second = 0
                        minute += 1
                    }
                    if (minute.toString().length == 1) {
                        image2 = "cam_" + piControl.toString().substring(0,10) + "0" + minute + second + "0.jpg"
                        Picasso.get().load(image2).into(imageView)
                    }
                    else{
                        image2 = "cam_" + piControl.toString().substring(0,10) + minute + second + "0.jpg"
                        Picasso.get().load(image2).into(imageView)
                    }
                    second += 1
                    if(second == 6){
                        second = 0
                        minute += 1
                    }
                    if (minute.toString().length == 1) {
                        image3 = "cam_" + piControl.toString().substring(0,10) + "0" + minute + second + "0.jpg"
                        Picasso.get().load(image3).into(imageView)
                    }
                    else{
                        image3 = "cam_" + piControl.toString().substring(0,10) + minute + second + "0.jpg"
                        Picasso.get().load(image3).into(imageView)
                    }
                    getImage(image1,image2,image3)
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }
        hi.addListenerForSingleValueEvent(menuListener)
    }

    private fun getImage(cameraPath1: String, cameraPath2: String, cameraPath3: String) {
        val storage = Firebase.storage
        var storageRef = storage.reference
        storageRef.child("PI_01_CONTROL/" + cameraPath1).downloadUrl.addOnSuccessListener {
            val image = it.toString()
            Picasso.get().load(image).into(imageView)
            imageUrl1 = image
        }.addOnFailureListener {
            // Handle any errors
        }
        var storageRef2 = storage.reference
        storageRef2.child("PI_01_CONTROL/" + cameraPath2).downloadUrl.addOnSuccessListener {
            val image = it.toString()
            Picasso.get().load(image).into(imageView2)
            imageUrl2 = image
        }.addOnFailureListener {
            // Handle any errors
        }
        var storageRef3 = storage.reference
        storageRef3.child("PI_01_CONTROL/" + cameraPath3).downloadUrl.addOnSuccessListener {
            val image = it.toString()
            Picasso.get().load(image).into(imageView3)
            imageUrl3 = image
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            downloadImage(imageUrl1,imageUrl2,imageUrl3)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    // Download the Image
                    downloadImage(imageUrl1,imageUrl2,imageUrl3)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    var msg: String? = ""
    var lastMsg = ""

    private fun downloadImage(url: String,url2: String,url3:String) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)
        val downloadUri2 = Uri.parse(url2)
        val downloadUri3 = Uri.parse(url3)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }
        val request2 = DownloadManager.Request(downloadUri2).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url2.substring(url2.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url2.substring(url2.lastIndexOf("/") + 1)
                )
        }
        val request3 = DownloadManager.Request(downloadUri3).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url3.substring(url3.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url3.substring(url3.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
        val downloadId2 = downloadManager.enqueue(request2)
        val query2 = DownloadManager.Query().setFilterById(downloadId2)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query2)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url2, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
        val downloadId3 = downloadManager.enqueue(request3)
        val query3 = DownloadManager.Query().setFilterById(downloadId3)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query3)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url3, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

}
