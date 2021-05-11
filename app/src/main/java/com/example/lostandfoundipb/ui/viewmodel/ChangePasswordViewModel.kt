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
    lateinit var changePasswordErrorAuth: Confirmation.Result
    val changePasswordResult = MutableLiveData<Confirmation.Result>()
    lateinit var changePasswordDetail: Confirmation.Result

    fun changePassword(api: ApiService, password: String, old_password: String) = viewModelScope.launch {
        val response = api.changePassword(password, old_password)
        changePasswordResult.postValue(handleChangePasswordResponse(response)!!)
    }

    private fun handleChangePasswordResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful) {
            response.body()
        }
        else {
            changePasswordErrorAuth = Confirmation.Result( false, response.message())
            changePasswordErrorAuth
        }
    }
}