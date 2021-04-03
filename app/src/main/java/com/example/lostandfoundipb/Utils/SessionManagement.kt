package com.example.lostandfoundipb.Utils

import android.content.Context
import android.content.SharedPreferences
import com.example.lostandfoundipb.retrofit.models.User

class SessionManagement (var context: Context){

    companion object{
        private val PREF_NAME = "AndroidHivePref"
        private val IS_LOGIN = "IsLoggedIn"
        val KEY_ID = "id"
        val KEY_EMAIL = "email"
        val KEY_NAME = "name"
        val KEY_USERNAME = "name"
        val KEY_TELEPHONE = "phone"
        val KEY_ROLE = "role"
        val KEY_NIM = "nim"
        val KEY_NIP = "nip"
        val KEY_FACULTY = "faculty"
        val KEY_DEPARTMENT = "department"
        val KEY_UNIT = "unit"
        val KEY_BATCH = "batch"
        val KEY_PICTURE = "picture"
        val KEY_TOKEN = "token"
        val KEY_PASS = "password"
    }


    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    init{
        pref = context.getSharedPreferences(PREF_NAME,0)
        editor = pref.edit()
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)

    val token: String
        get() {
            return pref.getString(KEY_TOKEN,"").toString()
        }

    val name: String
        get() {
            return pref.getString(KEY_NAME, "").toString()
        }


    fun createLogin(token: String, password: String){
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_TOKEN,token)
        editor.putString(KEY_PASS,password)
        editor.commit()
    }

    fun createSession(user: User.User){
        editor.putString(KEY_ID, user.id)
        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_USERNAME, user.username)
        editor.putString(KEY_TELEPHONE, user.telephone)
        editor.putString(KEY_ROLE, user.role)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PICTURE, user.picture)

        if(user.role=="student"){
            editor.putString(KEY_NIM, user.student?.nim)
            editor.putString(KEY_FACULTY, user.student?.faculty)
            editor.putString(KEY_DEPARTMENT, user.student?.department)
            editor.putString(KEY_BATCH, user.student?.batch.toString())
        }
        else if (user.role=="lecturer"){
            editor.putString(KEY_NIP, user.lecturer?.nip)
            editor.putString(KEY_FACULTY, user.lecturer?.faculty)
            editor.putString(KEY_DEPARTMENT, user.lecturer?.department)
        }
        else if (user.role=="staff"){
            editor.putString(KEY_NIP, user.staff?.nip)
            editor.putString(KEY_FACULTY, user.staff?.unit)
        }

        editor.commit()

    }

    fun checkLogin(): Boolean = this.isLoggedIn

}