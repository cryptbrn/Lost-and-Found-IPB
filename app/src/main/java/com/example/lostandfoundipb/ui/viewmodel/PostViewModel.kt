package com.example.lostandfoundipb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Post
import kotlinx.coroutines.launch
import retrofit2.Response

class PostViewModel : ViewModel() {
    val postResult = MutableLiveData<Post.Result>()
    lateinit var errorPost: Post.Result

    fun getPost(api: ApiService) = viewModelScope.launch {
        val response = api.getPost()
        postResult.postValue(handlePostResponse(response)!!)
    }

    private fun handlePostResponse(response: Response<Post.Result>): Post.Result? {
        return if(response.isSuccessful){
            response.body()
        }
        else{
            errorPost = Post.Result(false,response.message(),null)
            errorPost
        }
    }
}