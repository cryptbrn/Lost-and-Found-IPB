package com.example.lostandfoundipb.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.retrofit.Global.Companion.BASE_URL
import com.example.lostandfoundipb.retrofit.Global.Companion.URL_PICT
import kotlinx.android.synthetic.main.activity_url.*
import org.jetbrains.anko.toast

class UrlActivity : AppCompatActivity() {
    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)

        btn_url.setOnClickListener {
            url = url_et.text.toString()

            if (!TextUtils.isEmpty(url)) {
                BASE_URL =url+"api/"
                URL_PICT = url+"storage/user_picture/"
                toast("Url Changed")
            }
            toast(BASE_URL)
        }

    }
}