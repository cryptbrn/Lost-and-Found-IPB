package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Confirmation {
    @Parcelize
    data class Result(
            val success: Boolean,
            val message: String
    ):Parcelable
}