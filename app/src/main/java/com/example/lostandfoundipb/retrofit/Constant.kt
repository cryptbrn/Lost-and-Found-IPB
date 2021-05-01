package com.example.lostandfoundipb.retrofit

import android.app.Application


class Global : Application(){
    companion object {
        var BASE_URL = "https://3a3a510f308f.ngrok.io/api/"
        var URL_PICT = "https://3a3a510f308f.ngrok.io/storage/user_picture/"
        var URL_POST = "https://3a3a510f308f.ngrok.io/storage/item_picture/"
    }
}

