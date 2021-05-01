package com.example.lostandfoundipb.retrofit

import android.app.Application


class Global : Application(){
    companion object {
        var BASE_URL = "https://8fad1ac97be8.ngrok.io/api/"
        var URL_PICT = "https://8fad1ac97be8.ngrok.io/storage/user_picture/"
        var URL_POST = "https://8fad1ac97be8.ngrok.io/storage/item_picture/"
    }
}

