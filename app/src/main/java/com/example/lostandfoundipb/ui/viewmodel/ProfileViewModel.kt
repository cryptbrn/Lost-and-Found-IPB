package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    val logoutResult = MutableLiveData<Confirmation.Result>()
    lateinit var errorLogout: Confirmation.Result

    fun logout(api: ApiService) = viewModelScope.launch {
        val response = api.logout()
        logoutResult.postValue(handleLogoutResponse(response)!!)
    }

    private fun handleLogoutResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorLogout = Confirmation.Result(false,response.message())
            errorLogout
        }
    }
}