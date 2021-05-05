package com.example.lostandfoundipb.Utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.lostandfoundipb.retrofit.models.User
import com.example.lostandfoundipb.ui.LoginActivity

class SessionManagement (var context: Context){

    companion object{
        private val PREF_NAME = "AndroidHivePref"
        private val IS_LOGIN = "IsLoggedIn"
        val KEY_ID = "id"
        val KEY_EMAIL = "email"
        val KEY_NAME = "name"
        val KEY_USERNAME = "username"
        val KEY_TELEPHONE = "telephone"
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


    val user: HashMap<String,String>
        get() {
            val  user = HashMap<String , String>()
            user[KEY_EMAIL] = pref.getString(KEY_EMAIL, "").toString()
            user[KEY_ID] = pref.getString(KEY_ID, "").toString()
            user[KEY_NAME] = pref.getString(KEY_NAME, "").toString()
            user[KEY_USERNAME] = pref.getString(KEY_USERNAME, "").toString()
            user[KEY_TELEPHONE] = pref.getString(KEY_TELEPHONE, "").toString()
            user[KEY_ROLE] = pref.getString(KEY_ROLE, "").toString()
            user[KEY_PICTURE] = pref.getString(KEY_PICTURE, "").toString()
            user[KEY_NIP] = pref.getString(KEY_NIP, "").toString()
            user[KEY_BATCH] = pref.getString(KEY_BATCH, "").toString()
            user[KEY_UNIT] = pref.getString(KEY_UNIT, "").toString()
            user[KEY_FACULTY] = pref.getString(KEY_FACULTY, "").toString()
            user[KEY_DEPARTMENT] = pref.getString(KEY_DEPARTMENT, "").toString()
            user[KEY_NIM] = pref.getString(KEY_NIM, "").toString()
            return user
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

        when (user.role) {
            "student" -> {
                editor.putString(KEY_NIM, user.student?.nim)
                editor.putString(KEY_FACULTY, user.student?.faculty)
                editor.putString(KEY_DEPARTMENT, user.student?.department)
                editor.putString(KEY_BATCH, user.student?.batch.toString())
            }
            "lecturer" -> {
                editor.putString(KEY_NIP, user.lecturer?.nip)
                editor.putString(KEY_FACULTY, user.lecturer?.faculty)
                editor.putString(KEY_DEPARTMENT, user.lecturer?.department)
            }
            "staff" -> {
                editor.putString(KEY_NIP, user.staff?.nip)
                editor.putString(KEY_UNIT, user.staff?.unit)
            }
        }

        editor.commit()

    }

    fun updateUser(user: User.User){
        editor.remove(KEY_NAME)
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_TELEPHONE)

        when (user.role) {
            "student" -> {
                editor.remove(KEY_NIM)
                editor.remove(KEY_FACULTY)
                editor.remove(KEY_DEPARTMENT)
                editor.remove(KEY_BATCH)
            }
            "lecturer" -> {
                editor.remove(KEY_NIP)
                editor.remove(KEY_FACULTY)
                editor.remove(KEY_DEPARTMENT)
            }
            "staff" -> {
                editor.remove(KEY_NIP)
                editor.remove(KEY_UNIT)
            }
        }
        editor.commit()

        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_USERNAME, user.username)
        editor.putString(KEY_TELEPHONE, user.telephone)

        when (user.role) {
            "student" -> {
                editor.putString(KEY_NIM, user.student?.nim)
                editor.putString(KEY_FACULTY, user.student?.faculty)
                editor.putString(KEY_DEPARTMENT, user.student?.department)
                editor.putString(KEY_BATCH, user.student?.batch.toString())
            }
            "lecturer" -> {
                editor.putString(KEY_NIP, user.lecturer?.nip)
                editor.putString(KEY_FACULTY, user.lecturer?.faculty)
                editor.putString(KEY_DEPARTMENT, user.lecturer?.department)
            }
            "staff" -> {
                editor.putString(KEY_NIP, user.staff?.nip)
                editor.putString(KEY_UNIT, user.staff?.unit)
            }
        }

        editor.commit()

    }

    fun checkLogin(): Boolean = this.isLoggedIn
    fun clearSession() {
        editor.remove(IS_LOGIN)
        editor.remove(KEY_TOKEN)
        editor.remove(KEY_PASS)
        editor.remove(KEY_ID)
        editor.remove(KEY_NAME)
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_TELEPHONE)
        editor.remove(KEY_ROLE)
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_PICTURE)
        editor.remove(KEY_NIM)
        editor.remove(KEY_FACULTY)
        editor.remove(KEY_DEPARTMENT)
        editor.remove(KEY_BATCH)
        editor.remove(KEY_NIP)
        editor.remove(KEY_UNIT)
        editor.commit()
    }

    fun logout(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}