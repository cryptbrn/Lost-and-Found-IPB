package com.example.lostandfoundipb.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.adapters.PostAdapter
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Post
import com.example.lostandfoundipb.ui.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast

class SearchActivity : AppCompatActivity() {
    lateinit var adapterPost: PostAdapter
    lateinit var postRv: RecyclerView
    private var post: MutableList<Post.Post> = mutableListOf()
    private var postAll: MutableList<Post.Post> = mutableListOf()
    private var postData: MutableList<Post.Post> = mutableListOf()
    lateinit var session: SessionManagement
    private lateinit var refresh: SwipeRefreshLayout
    var type: Boolean = false
    lateinit var category: String
    lateinit var searchSv: SearchView
    private val apiService by lazy {
        ApiService.create(this)
    }
    private lateinit var viewModel : PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        type = intent.getBooleanExtra("type",false)
        category = intent.getStringExtra("category")!!
        session = SessionManagement(this)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
        supportActionBar?.title = getString(R.string.search_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        onClick()
        getPost()
        postResult()
    }

    private fun onClick() {
        search_btn_create_new.setOnClickListener {
            startActivity<CreatePostActivity>("type" to type, "category" to category)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun init() {
        searchSv = findViewById(R.id.search_sv)
        postRv = findViewById(R.id.search_rv)
        refresh = findViewById(R.id.search_swipe_refresh)
        search_tv_category.text = getString(R.string.filtered_by) + category + getString(R.string.quotes)
        adapterPost = PostAdapter(post, this)
        postRv.adapter = adapterPost
        postRv.layoutManager = LinearLayoutManager(this)

        search()

        refresh.onRefresh {
            refresh.isRefreshing = false
            getPost()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun search() {
        searchSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showProgress(true)
                post.clear()
                for (data in postAll){
                    if(data.title.toLowerCase().contains(query.toString())||
                        data.description.toLowerCase().contains(query.toString())||
                        data.item.name.toLowerCase().contains(query.toString())){
                        post.add(data)
                    }
                }
                adapterPost.notifyDataSetChanged()
                showProgress(false)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }
        })
    }


    private fun getPost(){
        showProgress(true)
        viewModel.getPost(apiService)
    }

    private fun postResult(){
        viewModel.postResult.observe({ lifecycle }, {
            if (it.success) {
                post.clear()
                postAll.clear()
                postData.clear()
                postData.addAll(it.post!!)
                postData.sortWith{c1, c2 -> c2.updated_at.compareTo(c1.updated_at)}
                for (data in postData) {
                    if (!data.is_deleted) {
                        if(type){
                            if(!data.type && data.item.category==category){
                                postAll.add(data)
                                post.add(data)
                            }
                        }
                        else {
                            if(data.type && data.item.category==category){
                                postAll.add(data)
                                post.add(data)
                            }
                        }

                    }

                }
                adapterPost.notifyDataSetChanged()
                showProgress(false)

            } else {
                toast(it.message.toString())
                showProgress(false)
            }
        })
    }

    private fun showProgress(show: Boolean){
        search_progress.visibility = if(show) View.VISIBLE else View.GONE
        disableTouch(show)
    }

    private fun disableTouch(status: Boolean){
        if(status){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

}