package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.Global
import com.example.lostandfoundipb.retrofit.models.Confirmation
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class EditPostViewModel : ViewModel() {
    val editPostResult = MutableLiveData<Confirmation.Result>()
    lateinit var errorEditPost: Confirmation.Result

    fun editPost(api: ApiService, picture: MultipartBody.Part, update: Map<String, @JvmSuppressWildcards RequestBody>, id: String) = viewModelScope.launch {
        val response = api.editPost(Global.BASE_URL +"post/"+id,picture,update)
        editPostResult.postValue(handleEditPostResponse(response)!!)
    }

    fun editPost(api: ApiService, update: Map<String, @JvmSuppressWildcards RequestBody>, id: String) = viewModelScope.launch {
        val response = api.editPost(Global.BASE_URL +"post/"+id, update)
        editPostResult.postValue(handleEditPostResponse(response)!!)
    }

    private fun handleEditPostResponse(response: Response<Confirmation.Result>): Confirmation.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorEditPost = Confirmation.Result(false,response.message())
            errorEditPost
        }
    }
}