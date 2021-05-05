package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.User
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {
    val authResult = MutableLiveData<User.Result>()
    lateinit var errorAuth: User.Result

    fun auth(api: ApiService) = viewModelScope.launch {
        val response = api.auth()
        authResult.postValue(handleAuthResponse(response)!!)
    }

    private fun handleAuthResponse(response: Response<User.Result>): User.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorAuth = User.Result(false,response.message(),null,null)
            errorAuth
        }
    }
}