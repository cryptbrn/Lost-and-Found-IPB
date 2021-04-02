package com.example.lostandfoundipb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.noAnimation

class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(intentFor<LoginActivity>().noAnimation())
        finish()
    }

}