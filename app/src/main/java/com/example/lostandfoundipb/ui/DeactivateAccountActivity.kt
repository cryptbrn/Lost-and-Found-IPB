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
import com.example.lostandfoundipb.ui.viewmodel.DeactivateAccountViewModel
import kotlinx.android.synthetic.main.activity_deactivate_account.*
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class DeactivateAccountActivity: AppCompatActivity() {
    lateinit var deactConfirm: String
    lateinit var deactPassword: String
    lateinit var session: SessionManagement

    lateinit var viewModel: DeactivateAccountViewModel
    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deactivate_account)
        viewModel = ViewModelProviders.of(this).get(DeactivateAccountViewModel::class.java)
        supportActionBar?.title = getString(R.string.deactivate_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        session = SessionManagement(this)

        deactivateAccountResult()
        onClick()
    }

    private fun onClick() {
        deactivate_account_button.setOnClickListener { deactivateAccount() }
    }

    private fun deactivateAccount() {
        deactivation_confirmation.error = null
        deactivation_password.error = null

        var cancel = false
        var focusView: View? = null

        deactConfirm = deactivation_confirmation.text.toString()
        deactPassword = deactivation_password.text.toString()

        if(TextUtils.isEmpty(deactConfirm)) {
            deactivation_confirmation.error = getString(R.string.error_empty)
            focusView = deactivation_confirmation
            cancel = true
        }

        if(deactConfirm != "deactivate") {
            deactivation_confirmation.error = getString(R.string.deact_confirm_error)
            focusView = deactivation_confirmation
            cancel = true
        }

        if(TextUtils.isEmpty(deactPassword)) {
            deactivation_password.error = getString(R.string.error_empty)
            focusView = deactivation_password
            cancel = true
        }

        if(deactPassword != session.password) {
            deactivation_password.error = getString(R.string.deact_password_error)
            focusView = deactivation_password
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            attemptDeactivateAccount()
        }
    }

    private fun attemptDeactivateAccount() {
        showProgress(true)
        viewModel.deactivateAccount(apiService)
    }

    private fun deactivateAccountResult(){
        viewModel.deactivateAccountResult.observe({lifecycle}, {result ->
            if(result.success) {
                showProgress(false)
                alert("Your account has been successfully deactivated."){
                    title = "Account Deactivation Complete"
                    yesButton{
                        session.clearSession()
                        session.logout()
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
        deactivate_account_progress.visibility = if(show) View.VISIBLE else View.GONE
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