package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Staff {
    @Parcelize
    data class Staff(
            val nip: String,
            val unit: String
    ): Parcelable
}