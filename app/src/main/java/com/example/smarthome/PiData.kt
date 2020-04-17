package com.example.smarthome
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PiData (
    val humid: String,
    val light: String,
    val sound: String,
    val tempe: String,
    val ultra: String
): Parcelable {
    constructor() : this("", "", "", "", "")
}