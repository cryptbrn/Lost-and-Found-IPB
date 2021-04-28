package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Confirmation
import com.example.lostandfoundipb.retrofit.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileViewModel : ViewModel() {
    private var disposable: Disposable? = null
    private var job = Job()
    private var mainScope = CoroutineScope(job + Dispatchers.Main)


    private val edit_detail_result = MutableLiveData<Confirmation.Result>()
    val editDetailResult: LiveData<Confirmation.Result> = edit_detail_result

    private val edit_detail_string = MutableLiveData<String>()
    val editDetailString: LiveData<String> = edit_detail_string

    private val auth_result = MutableLiveData<User.Result>()
    val authResult: LiveData<User.Result> = auth_result

    private val auth_string = MutableLiveData<String>()
    val authString: LiveData<String> = auth_string

    fun editProfile(apiService: ApiService, update: Map<String, @JvmSuppressWildcards RequestBody>){
        mainScope.launch {
            disposable = apiService.editProfile(update)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    edit_detail_result.value = result
                    edit_detail_string.value = "Success"
                }, { error ->
                    edit_detail_string.value= error.toString()
                })
        }
    }

    fun editProfile(apiService: ApiService, picture: MultipartBody.Part, update: Map<String, @JvmSuppressWildcards RequestBody>){
        mainScope.launch {
            disposable = apiService.editProfile(picture,update)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        edit_detail_result.value = result
                        edit_detail_string.value = "Success"
                    }, { error ->
                        edit_detail_string.value= error.toString()
                    })
        }
    }

    fun auth(apiService: ApiService){
        mainScope.launch {
            disposable = apiService.auth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    auth_result.value = result
                    auth_string.value = "Success"
                }, { error ->
                    auth_string.value= error.toString()
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