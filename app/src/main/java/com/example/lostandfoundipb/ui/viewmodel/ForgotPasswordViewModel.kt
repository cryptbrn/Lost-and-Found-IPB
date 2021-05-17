package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Confirmation
import kotlinx.coroutines.launch
import retrofit2.Response

class ForgotPasswordViewModel : ViewModel() {
    lateinit var forgotPasswordErrorAuth: Confirmation.Result
    val forgotPasswordResult = MutableLiveData<Confirmation.Result>()

    fun forgotPassword(api: ApiService, email: String) = viewModelScope.launch {
        val response = api.forgotPassword(email)
        forgotPasswordResult.postValue(handleForgotPasswordResponse(response)!!)
    }

    private fun handleForgotPasswordResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful) {
            response.body()
        }
        else {
            forgotPasswordErrorAuth = Confirmation.Result( false, response.message())
            forgotPasswordErrorAuth
        }
    }


}