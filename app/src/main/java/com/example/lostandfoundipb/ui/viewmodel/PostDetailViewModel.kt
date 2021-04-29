package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.Global.Companion.BASE_URL
import com.example.lostandfoundipb.retrofit.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostDetailViewModel: ViewModel() {
    private var disposable: Disposable? = null
    private var job = Job()
    private var mainScope = CoroutineScope(job + Dispatchers.Main)


    private val person_result = MutableLiveData<User.Result>()
    val personResult: LiveData<User.Result> = person_result

    private val person_string = MutableLiveData<String>()
    val personString: LiveData<String> = person_string

    fun getPerson(apiService: ApiService, id: String){
        mainScope.launch {
            disposable = apiService.getPerson(BASE_URL+"user/"+id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        person_result.value = result
                        person_string.value = "Success"
                    }, { error ->
                        person_string.value= error.toString()
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