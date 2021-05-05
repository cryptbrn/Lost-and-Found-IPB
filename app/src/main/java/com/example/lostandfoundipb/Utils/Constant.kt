package com.example.lostandfoundipb.Utils

import android.app.Application


class Global : Application(){
    companion object {
        var BASE_URL = "https://lostandfoundipb.herokuapp.com/api/"
        var URL_PICT = "https://lostandfoundipb.herokuapp.com//storage/user_picture/"
        var URL_POST = "https://lostandfoundipb.herokuapp.com//storage/item_picture/"
    }
}

