package com.example.lostandfoundipb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.se.omapi.Session
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.Utils.passwordValidator
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.ui.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var passLama = String
    lateinit var passBaru = String
    lateinit var konfirmPassBaru = String
    lateinit var viewModel = ChangePasswordViewModel
    lateinit var session = SessionManagement

    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
        supportActionBar?.title = getString(R.string.change_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onClick() {
        change_password_button_save.setOnClickListener { updatePassword() }
    }

    private fun updatePassword() {
        change_password_pass_lama.error = null
        change_password_pass_baru.error = null
        change_password_konfirm_pass.error = null

        var cancel = false
        var focusView: View? = null

        passLama = change_password_pass_lama.text.toString()
        passBaru = change_password_pass_baru.text.toString()
        konfirmPassBaru = change_password_konfirm_pass.text.toString()

        if(TextUtils.isEmpty(passLama)) {
            change_password_pass_lama.error = getString(R.string.error_empty)
            focusView = change_password_pass_lama
            cancel = true
        }
        else if(!passwordValidator(passLama)) {
            change_password_pass_lama.error = getString(R.string.password_error)
            focusView = change_password_pass_lama
            cancel = true
        }

        if(TextUtils.isEmpty(passBaru)) {
            change_password_pass_baru.error = getString(R.string.error_empty)
            focusView = change_password_pass_baru
            cancel = true
        }

        if(TextUtils.isEmpty(konfirmPassBaru)) {
            change_password_konfirm_pass.error = getString(R.string.error_empty)
            focusView = change_password_konfirm_pass
            cancel = true
        }

        if(passBaru != passLama) {
            if(passBaru != konfirmPassBaru) {
                change_password_pass_baru.error = getString(R.string.password_not_match)
                focusView = change_password_pass_baru
                focusView = change_password_konfirm_pass
                cancel = true
            } else {
                change_password_pass_baru.error = getString(R.string.password_error)
                focusView = change_password_pass_baru
                cancel = true
            }
        } else {
            change_password_pass_baru.error = getString(R.string.password_error)
        }
    }

    private fun showProgress(show: Boolean){
        change_password_progress.visibility = if(show) View.VISIBLE else View.GONE
        disableTouch(show)
    }

    fun disableTouch(status: Boolean){
        if(status){
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

}