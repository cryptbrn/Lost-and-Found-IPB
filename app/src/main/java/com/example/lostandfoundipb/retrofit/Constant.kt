package com.example.lostandfoundipb.retrofit

import android.app.Application


class Global : Application(){
    companion object {
        var BASE_URL = "https://f72820c6b260.ngrok.io/api/"
        var URL_PICT = "https://f72820c6b260.ngrok.io/storage/user_picture/"
    }
}

