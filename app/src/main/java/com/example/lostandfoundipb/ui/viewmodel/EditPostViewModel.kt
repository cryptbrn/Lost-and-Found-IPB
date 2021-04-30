package com.example.lostandfoundipb.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.Global
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

class EditPostViewModel : ViewModel() {
    private var disposable: Disposable? = null
    private var job = Job()
    private var mainScope = CoroutineScope(job + Dispatchers.Main)


    private val edit_post_result = MutableLiveData<Confirmation.Result>()
    val editPostResult: LiveData<Confirmation.Result> = edit_post_result

    private val edit_post_string = MutableLiveData<String>()
    val editPostString: LiveData<String> = edit_post_string



    fun editPost(apiService: ApiService, picture: MultipartBody.Part, update: Map<String, @JvmSuppressWildcards RequestBody>, id: String){
        mainScope.launch {
            disposable = apiService.editPost(Global.BASE_URL +"post/"+id,picture,update)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    edit_post_result.value = result
                    edit_post_string.value = "Success"
                }, { error ->
                    edit_post_string.value= error.toString()
                })
        }
    }

    fun editPost(apiService: ApiService, update: Map<String, @JvmSuppressWildcards RequestBody>, id: String){
        mainScope.launch {
            disposable = apiService.editPost(Global.BASE_URL +"post/"+id, update)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    edit_post_result.value = result
                    edit_post_string.value = "Success"
                }, { error ->
                    edit_post_string.value= error.toString()
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