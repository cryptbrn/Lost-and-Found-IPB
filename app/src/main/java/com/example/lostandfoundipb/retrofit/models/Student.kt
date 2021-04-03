package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Student {
    @Parcelize
    data class Student(
            val nim: String,
            val faculty: String,
            val department: String,
            val batch: Int
    ): Parcelable
}