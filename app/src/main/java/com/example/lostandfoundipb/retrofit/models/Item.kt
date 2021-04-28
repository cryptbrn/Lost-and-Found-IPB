package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Item {
    @Parcelize
    data class Item (
            var id: Int,
            var name: String,
            var post_id: Int,
            var category: String,
            var location: String,
            var picture: String,
            var updated_at: String,
    ): Parcelable
}
