package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {

    val loginResult = MutableLiveData<User.Result>()
    lateinit var errorLogin: User.Result

    fun login(api: ApiService, email: String, password: String) = viewModelScope.launch {
        val response = api.login(email,password)
        loginResult.postValue(handleLoginResponse(response)!!)
    }

    private fun handleLoginResponse(response: Response<User.Result>): User.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorLogin = User.Result(false,response.message(),null,null)
            errorLogin
        }
    }



}