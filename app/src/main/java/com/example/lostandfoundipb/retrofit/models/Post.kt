package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Post {
    @Parcelize
    data class Result(
            var success: Boolean,
            var message: String?,
            var post: List<Post>?
    ): Parcelable

    @Parcelize
    data class ResultId(
            var success: Boolean,
            var message: String,
            var post: Post?
    ): Parcelable

    @Parcelize
    data class Post(
            var id: Int,
            var user_id: Int,
            var title: String,
            var description: String,
            var status: Int,
            var type: Boolean,
            var is_deleted: Boolean,
            var updated_at: String,
            var item: Item.Item
    ): Parcelable
}