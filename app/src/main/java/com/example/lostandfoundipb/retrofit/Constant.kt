package com.example.lostandfoundipb.retrofit

import android.app.Application


class Global : Application(){
    companion object {
        var BASE_URL = "https://12fd59f9579b.ngrok.io/api/"
        var URL_PICT = "https://12fd59f9579b.ngrok.io/storage/user_picture/"
        var URL_POST = "https://12fd59f9579b.ngrok.io/storage/item_picture/"
    }
}

