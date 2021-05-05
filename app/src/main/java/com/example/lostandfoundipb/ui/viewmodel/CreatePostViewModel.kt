package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Confirmation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class CreatePostViewModel : ViewModel() {
    val createPostResult = MutableLiveData<Confirmation.Result>()
    lateinit var errorAuth: Confirmation.Result

    fun post(api: ApiService, picture: MultipartBody.Part, update: Map<String, @JvmSuppressWildcards RequestBody>) = viewModelScope.launch {
        val response = api.post(picture,update)
        createPostResult.postValue(handleCreatePostResponse(response)!!)
    }

    private fun handleCreatePostResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorAuth = Confirmation.Result(false,response.message())
            errorAuth
        }
    }
}