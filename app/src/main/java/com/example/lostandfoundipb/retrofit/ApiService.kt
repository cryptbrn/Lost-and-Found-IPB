package com.example.lostandfoundipb.retrofit

import com.example.lostandfoundipb.retrofit.models.Confirmation
import com.example.lostandfoundipb.retrofit.models.User
import okhttp3.RequestBody
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun register(@Body body: RequestBody):
            Observable<Confirmation.Result>

    @POST("login")
    fun login(
            @Field("email") email:String,
            @Field("password") password:String
    ):
            Observable<User.Result>

    @GET("auth")
    fun auth():
            Observable<User.Result>

}

