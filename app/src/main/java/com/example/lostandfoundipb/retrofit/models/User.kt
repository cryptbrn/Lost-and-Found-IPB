package com.example.lostandfoundipb.retrofit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object User {
    @Parcelize
    data class Result(
            var success: Boolean,
            val message: String,
            val token: String,
            val user: User
    ):Parcelable

    @Parcelize
    data class User(
        val id: String,
        val name: String,
        val username: String,
        val telephone: String,
        val role: String,
        val email: String,
        val picture: String,
        val staff: Staff.Staff?,
        val lecturer: Lecturer.Lecturer?,
        val student: Student.Student?
    ):Parcelable

    @Parcelize
    data class SignUp(
        val name: String,
        val username: String,
        val email: String,
        val telephone: String,
        val password: String,
        val role: String,
        val nim: String?,
        val nip: String?,
        val faculty: String?,
        val department: String?,
        val unit: String?,
        val batch: Int?
        ): Parcelable

    @Parcelize
    data class Update(
        val id: Int,
        val name: String,
        val username: String,
        val telephone: String,
        val staff: Staff.Staff?,
        val lecturer: Lecturer.Lecturer?,
        val student: Student.Student?
    ): Parcelable






}