package com.example.lostandfoundipb.retrofit

import android.content.Context
import com.example.lostandfoundipb.BuildConfig
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.retrofit.models.Confirmation
import com.example.lostandfoundipb.retrofit.models.User
import okhttp3.RequestBody
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    @POST("register")
    fun register(@Body body: User.SignUp):
            Observable<Confirmation.Result>

    @FormUrlEncoded
    @POST("login")
    fun login(
            @Field("email") email:String,
            @Field("password") password:String
    ):
            Observable<User.Result>

    @GET("auth")
    fun auth():
            Observable<User.Result>

    @POST("logout")
    fun logout():
            Observable<Confirmation.Result>


    companion object{
        lateinit var session: SessionManagement

        fun create(context: Context): ApiService{
            val interceptor =  HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            session = SessionManagement(context)

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(headersInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            return  retrofit.create(ApiService::class.java)
        }

        private fun headersInterceptor(): Interceptor {
            return  if(session.token.isEmpty()){
                Interceptor { chain ->
                    chain.proceed(chain.request().newBuilder().build())
                }
            } else {
                Interceptor { chain ->
                    chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer "+session.token)
                        .build())
                }
            }
        }
    }
}

