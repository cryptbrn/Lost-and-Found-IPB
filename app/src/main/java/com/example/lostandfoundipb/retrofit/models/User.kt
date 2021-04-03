package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object User {
    @Parcelize
    data class Result(
            val success: Boolean,
            val message: String,
            val token: String,
            val user: User
    ):Parcelable

    @Parcelize
    data class User(
        val x:String
    ):Parcelable

}