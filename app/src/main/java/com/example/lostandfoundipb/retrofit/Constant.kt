package com.example.lostandfoundipb.retrofit

import android.app.Application


class Global : Application(){
    companion object {
        var BASE_URL = "https://d2a7ec7194c3.ngrok.io/api/"
        var URL_PICT = "https://d2a7ec7194c3.ngrok.io/storage/user_picture/"
        var URL_POST = "https://d2a7ec7194c3.ngrok.io/storage/item_picture/"
    }
}

