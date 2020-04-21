package com.example.smarthome
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PiControl (
    val buzzer: String,
    val camera: String,
    val lcd: String,
    val lcdtext: String,
    val led: String,
    val relay: String
): Parcelable {
    constructor() : this("0", "0", "1", "=App is running=", "0", "0")
}