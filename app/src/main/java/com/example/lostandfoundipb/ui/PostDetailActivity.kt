package com.example.lostandfoundipb.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.formatDate
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.Global.Companion.URL_POST
import com.example.lostandfoundipb.retrofit.models.Post
import com.example.lostandfoundipb.ui.viewmodel.PostDetailViewModel
import kotlinx.android.synthetic.main.activity_post_detail.*


class PostDetailActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create(this)
    }
    private lateinit var post: Post.Post
    private lateinit var viewModel: PostDetailViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        post = intent.getParcelableExtra("post")!!
        viewModel = ViewModelProviders.of(this).get(PostDetailViewModel::class.java)

        supportActionBar?.title = post.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        detail_title.text = post.title
        detail_category.text = getString(R.string.category_1)+ post.item.category
        detail_location.text = post.item.location
        detail_date.text = formatDate(post.item.updated_at,this)

        post.item.picture.let { setImage(it) }


    }

    private fun setImage(url: String) {
        Glide.with(this)
                .load(URL_POST + url)
                .apply(RequestOptions().centerCrop())
                .into(detail_picture)
    }

}