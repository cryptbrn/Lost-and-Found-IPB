package com.example.lostandfoundipb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.Utils.emailValidator
import com.example.lostandfoundipb.Utils.passwordValidator
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.ApiService.Companion.session
import com.example.lostandfoundipb.retrofit.Global.Companion.BASE_URL
import com.example.lostandfoundipb.retrofit.models.User
import com.example.lostandfoundipb.ui.viewmodel.LoginViewModel
import com.example.lostandfoundipb.ui.viewmodel.RegisterViewModel
import org.jetbrains.anko.startActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class   LoginActivity : AppCompatActivity() {
    lateinit var email: String
    lateinit var username: String
    lateinit var password: String
    lateinit var session: SessionManagement

    lateinit var viewModel: LoginViewModel
    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        session = SessionManagement(applicationContext)
        if(session.checkLogin()){
            startActivity<MainActivity>()
            finish()
        }
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        onClick()
    }

    private fun onClick() {
        login_register.setOnClickListener { startActivity<RegisterActivity>() }
        login_btn_login.setOnClickListener {
            toast(BASE_URL)
            checkLogin()
        }
        url_tv.setOnClickListener { startActivity<UrlActivity>()}
    }

    private fun checkLogin() {
        login_username.error = null
        login_password.error = null

        var cancel = false
        var focusView: View? = null

        email = login_username.text.toString()
        password = login_password.text.toString()

        if (TextUtils.isEmpty(email)) {
            login_username.error = getString(R.string.error_empty)
            focusView = login_username
            cancel = true
        }
        else if (!TextUtils.isEmpty(email) && !emailValidator(email)) {
            login_username.error = getString(R.string.email_error)
            focusView = login_username
            cancel = true
        }

        if (TextUtils.isEmpty(password)) {
            login_password.error = getString(R.string.error_empty)
            focusView = login_password
            cancel = true
        }
        else if (!passwordValidator(password)) {
            login_password.error = getString(R.string.password_error)
            focusView = login_password
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            attemptLogin(email, password)
        }

    }

    private fun attemptLogin(email: String, password: String) {
        showProgress(true)
        viewModel.login(apiService, email, password)
        viewModel.loginString.observe({lifecycle},{s ->
            if(s == "Success"){
                viewModel.loginResult.observe({lifecycle},{
                    if(it.success){
                        showProgress(false)
                        session.createLogin(it.token, password)
                        startActivity<MainActivity>()
                        finish()
                    }
                    else{
                        alert(it.message){
                            yesButton {  }
                        }.show()
                        showProgress(false)
                    }
                })
            }
            else{
                s.let {
                    alert(it){
                        yesButton {  }
                    }.show()
                }
                showProgress(false)
            }
        })
    }


    private fun showProgress(show: Boolean){
        login_progress.visibility = if(show) View.VISIBLE else View.GONE
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