package com.example.lostandfoundipb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lostandfoundipb.R
import org.jetbrains.anko.startActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_register.setOnClickListener { startActivity<RegisterActivity>() }
        login_btn_login.setOnClickListener { startActivity<MainActivity>() }
    }
}