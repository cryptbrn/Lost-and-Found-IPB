package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Lecturer {
    @Parcelize
    data class Lecturer(
            val nip: String,
            val faculty: String,
            val department: String
    ): Parcelable
}