package com.example.lostandfoundipb.retrofit

import android.app.Application


class Global : Application(){
    companion object {
        var BASE_URL = "https://4d0177d4724c.ngrok.io/api/"
        var URL_PICT = "https://4d0177d4724c.ngrok.io/storage/user_picture/"
        var URL_POST = "https://4d0177d4724c.ngrok.io/storage/item_picture/"
    }
}

