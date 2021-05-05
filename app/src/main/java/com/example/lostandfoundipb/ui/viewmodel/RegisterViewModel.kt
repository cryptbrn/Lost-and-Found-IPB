package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Confirmation
import com.example.lostandfoundipb.retrofit.models.User
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel : ViewModel(){
    val registerResult = MutableLiveData<Confirmation.Result>()
    lateinit var errorRegister: Confirmation.Result

    fun register(api: ApiService, user:User.SignUp) = viewModelScope.launch {
        val response = api.register(user)
        registerResult.postValue(handleRegisterResponse(response)!!)
    }

    private fun handleRegisterResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorRegister = Confirmation.Result(false,response.message())
            errorRegister
        }
    }
}