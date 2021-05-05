package com.example.lostandfoundipb.retrofit

import android.content.Context
import com.example.lostandfoundipb.Utils.Global.Companion.BASE_URL
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.retrofit.models.Confirmation
import com.example.lostandfoundipb.retrofit.models.Post
import com.example.lostandfoundipb.retrofit.models.User
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    @POST("register")
    suspend fun register(@Body body: User.SignUp):
            Response<Confirmation.Result>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
            @Field("email") email:String,
            @Field("password") password:String
    ):
            Response<User.Result>

    @GET("auth")
    suspend fun auth():
            Response<User.Result>

    @POST("logout")
    suspend fun logout():
            Response<Confirmation.Result>

    @Multipart
    @POST("user")
    suspend fun editProfile(@PartMap form: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<Confirmation.Result>

    @Multipart
    @POST("user")
    suspend fun editProfile(@Part picture: MultipartBody.Part,
                    @PartMap form: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<Confirmation.Result>

    @GET("post")
    suspend fun getPost():
            Response<Post.Result>

    @GET
    suspend fun getPerson(@Url url: String):
            Response<User.Result>

    @Multipart
    @POST("post/create-new")
    suspend fun post(@Part picture: MultipartBody.Part,
             @PartMap form: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<Confirmation.Result>

    @DELETE
    suspend fun deletePost(@Url url:String):
            Response<Confirmation.Result>

    @Multipart
    @POST
    suspend fun editPost(@Url url: String,
                 @Part picture: MultipartBody.Part,
                 @PartMap form: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<Confirmation.Result>

    @Multipart
    @POST
    suspend fun editPost(@Url url: String,
                 @PartMap form: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<Confirmation.Result>


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

