package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Confirmation
import kotlinx.coroutines.launch
import retrofit2.Response

class DeactivateAccountViewModel : ViewModel() {
    lateinit var deactivateAccountErrorAuth: Confirmation.Result
    val deactivateAccountResult = MutableLiveData<Confirmation.Result>()

    fun deactivateAccount(api: ApiService) = viewModelScope.launch {
        val response = api.deactivateAccount()
        deactivateAccountResult.postValue(handleDeactivateAccountResponse(response)!!)
    }

    private fun handleDeactivateAccountResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful) {
            response.body()
        }
        else {
            deactivateAccountErrorAuth = Confirmation.Result( false, response.message())
            deactivateAccountErrorAuth
        }
    }


}