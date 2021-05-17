package com.example.lostandfoundipb.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.Utils.emailValidator
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.ui.viewmodel.ForgotPasswordViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class ForgotPasswordActivity: AppCompatActivity() {
    lateinit var resetEmail: String
    lateinit var session: SessionManagement

    lateinit var viewModel: ForgotPasswordViewModel
    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
        supportActionBar?.title = getString(R.string.forgot_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        session = SessionManagement(this)

        forgotPasswordResult()
        onClick()
    }

    private fun onClick() {
        forgot_password_button.setOnClickListener { updatePassword() }
    }

    private fun updatePassword() {
        forgot_password_email.error = null

        var cancel = false
        var focusView: View? = null

        resetEmail = forgot_password_email.text.toString()

        if(TextUtils.isEmpty(resetEmail)) {
            forgot_password_email.error = getString(R.string.error_empty)
            focusView = forgot_password_email
            cancel = true
        }
        else if (!TextUtils.isEmpty(resetEmail) && !emailValidator(resetEmail)) {
            forgot_password_email.error = getString(R.string.email_error)
            focusView = forgot_password_email
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            attemptResetPassword(resetEmail)
        }
    }

    private fun attemptResetPassword(email: String) {
        showProgress(true)
        viewModel.forgotPassword(apiService, email)
    }

    private fun forgotPasswordResult(){
        viewModel.forgotPasswordResult.observe({lifecycle}, {result ->
            if(result.success) {
                showProgress(false)
                alert("We've sent an email to reset your password."){
                    title = "An Email has been sent"
                    yesButton{
                        finish()}
                }.show()
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
        forgot_password_progress.visibility = if(show) View.VISIBLE else View.GONE
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