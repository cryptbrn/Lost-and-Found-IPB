package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Confirmation
import com.example.lostandfoundipb.retrofit.models.User
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.Response

class ChangePasswordViewModel : ViewModel() {
    val authResult = MutableLiveData<User.Result>()
    lateinit var errorAuth: User.Result

    val editDetailResult = MutableLiveData<Confirmation.Result>()
    lateinit var errorEditDetail: Confirmation.Result

    fun auth(api: ApiService) = viewModelScope.launch {
        val response = api.auth()
        authResult.postValue(handleAuthResponse(response)!!)
    }

    fun changePassword(api: ApiService, update: Map<String, @JvmSuppressWildcards RequestBody>) = viewModelScope.launch {
        val response = api.editProfile(update)
        editDetailResult.postValue(handleEditDetailResponse(response)!!)
    }

    private fun handleAuthResponse(response: Response<User.Result>): User.Result? {
        return if(response.isSuccessful) {
            response.body()
        }
        else {
            errorAuth = User.Result( false, response.message(), null, null)
            errorAuth
        }
    }

    private fun handleEditDetailResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful) {
            response.body()
        }
        else {
            errorEditDetail = Confirmation.Result( false, response.message())
            errorEditDetail
        }
    }
}