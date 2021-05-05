package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.Global
import com.example.lostandfoundipb.retrofit.Global.Companion.BASE_URL
import com.example.lostandfoundipb.retrofit.models.Confirmation
import com.example.lostandfoundipb.retrofit.models.User
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response

class PostDetailViewModel: ViewModel() {
    val personResult = MutableLiveData<User.Result>()
    val deleteResult = MutableLiveData<Confirmation.Result>()
    val editPostResult = MutableLiveData<Confirmation.Result>()
    lateinit var errorPerson: User.Result
    lateinit var errorPost: Confirmation.Result

    fun getPerson(api: ApiService, id: String) = viewModelScope.launch {
        val response = api.getPerson(BASE_URL+"user/"+id)
        personResult.postValue(handlePersonResponse(response)!!)
    }

    fun deletePost(api: ApiService, id: String) = viewModelScope.launch {
        val response = api.deletePost(BASE_URL+"post/"+id)
        deleteResult.postValue(handlePostResponse(response)!!)
    }

    fun editPost(api: ApiService, update: Map<String, @JvmSuppressWildcards RequestBody>, id: String) = viewModelScope.launch {
        val response = api.editPost(Global.BASE_URL +"post-status/"+id, update)
        editPostResult.postValue(handlePostResponse(response)!!)
    }

    private fun handlePostResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorPost = Confirmation.Result(false,response.message())
            errorPost
        }
    }

    private fun handlePersonResponse(response: Response<User.Result>): User.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorPerson = User.Result(false,response.message(),null,null)
            errorPerson
        }
    }
}