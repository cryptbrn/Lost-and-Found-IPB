package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class CreatePostViewModel : ViewModel() {
    private var disposable: Disposable? = null
    private var job = Job()
    private var mainScope = CoroutineScope(job + Dispatchers.Main)


    private val create_post_result = MutableLiveData<Confirmation.Result>()
    val createPostResult: LiveData<Confirmation.Result> = create_post_result

    private val create_post_string = MutableLiveData<String>()
    val createPostString: LiveData<String> = create_post_string


    fun post(apiService: ApiService, picture: MultipartBody.Part, update: Map<String, @JvmSuppressWildcards RequestBody>){
        mainScope.launch {
            disposable = apiService.post(picture,update)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    create_post_result.value = result
                    create_post_string.value = "Success"
                }, { error ->
                    create_post_string.value= error.toString()
                })
        }
    }



    fun onPaused(){
        disposable?.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}