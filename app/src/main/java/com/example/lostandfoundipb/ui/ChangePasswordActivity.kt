package com.example.lostandfoundipb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.ui.viewmodel.ChangePasswordViewModel

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var passLama = String
    lateinit var passBaru = String
    lateinit var konfirmPassBaru = String
    lateinit var viewModel = ChangePasswordViewModel

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


}