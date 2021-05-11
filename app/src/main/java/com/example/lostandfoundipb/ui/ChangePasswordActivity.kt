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
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var oldPassword: String
    lateinit var password: String
    lateinit var confirmPassword: String
    lateinit var session: SessionManagement

    lateinit var viewModel: ChangePasswordViewModel
    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
        supportActionBar?.title = getString(R.string.change_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        session = SessionManagement(this)

        updatePasswordResult()
        onClick()
    }

    private fun onClick() {
        change_password_button_save.setOnClickListener { updatePassword() }
    }

    private fun updatePassword() {
        change_password_old.error = null
        change_password_new.error = null
        change_password_confirm.error = null

        var cancel = false
        var focusView: View? = null

        oldPassword = change_password_old.text.toString()
        password = change_password_new.text.toString()
        confirmPassword = change_password_confirm.text.toString()

        if(oldPassword != session.password) {
            change_password_old.error = getString(R.string.old_password_error)
            focusView = change_password_old
            cancel = true
        }

        if(TextUtils.isEmpty(oldPassword)) {
            change_password_old.error = getString(R.string.error_empty)
            focusView = change_password_old
            cancel = true
        }
        else if(!passwordValidator(oldPassword)) {
            change_password_old.error = getString(R.string.password_error)
            focusView = change_password_old
            cancel = true
        }

        if(TextUtils.isEmpty(password)) {
            change_password_new.error = getString(R.string.error_empty)
            focusView = change_password_new
            cancel = true
        }

        if(TextUtils.isEmpty(confirmPassword)) {
            change_password_confirm.error = getString(R.string.error_empty)
            focusView = change_password_confirm
            cancel = true
        }

        if(password != oldPassword) {
            if(password != confirmPassword) {
                change_password_new.error = getString(R.string.password_not_same)
                focusView = change_password_new
                focusView = change_password_confirm
                cancel = true
            }
        } else {
            change_password_new.error = getString(R.string.old_password_same)
            focusView = change_password_new
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            attemptUpdatePassword(password, oldPassword)
        }
    }

    private fun attemptUpdatePassword(password: String, old_password: String) {
        showProgress(true)
        viewModel.changePassword(apiService, password, old_password)
    }

    private fun updatePasswordResult(){
        viewModel.changePasswordResult.observe({lifecycle}, {result ->
            if(result.success) {
                showProgress(false)
                session.updatePassword(password)
                finish()
            } else {
                showProgress(false)
                result.let {
                    alert(it.message!!) {
                        yesButton { }
                    }.show()
                }
            }
        })
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