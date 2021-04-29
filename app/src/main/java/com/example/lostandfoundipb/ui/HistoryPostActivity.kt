package com.example.lostandfoundipb.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
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
import kotlinx.android.synthetic.main.activity_history_post.*
import org.jetbrains.anko.sdk27.coroutines.onQueryTextListener
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast

class HistoryPostActivity : AppCompatActivity() {
    lateinit var progress: LinearLayout
    lateinit var adapterPost: PostAdapter
    lateinit var postRv: RecyclerView
    private var post: MutableList<Post.Post> = mutableListOf()
    private var postAll: MutableList<Post.Post> = mutableListOf()
    lateinit var session: SessionManagement
    private lateinit var refresh: SwipeRefreshLayout
    lateinit var historySv: SearchView
    lateinit var searchQuery: String
    private val apiService by lazy {
        ApiService.create(this)
    }
    private lateinit var viewModel : PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_post)
        session = SessionManagement(this)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
        supportActionBar?.title = getString(R.string.post_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        getPost()
    }


    @SuppressLint("SetTextI18n")
    private fun init() {
        historySv = findViewById(R.id.history_sv)
        progress = findViewById(R.id.history_progress)
        postRv = findViewById(R.id.history_rv)
        refresh = findViewById(R.id.history_swipe_refresh)
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
        historySv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        viewModel.getPost(apiService!!)
        viewModel.postString.observe({ lifecycle }, { s ->
            if (s == "Success") {
                viewModel.postResult.observe({ lifecycle }, {
                    if (it.success) {
                        post.clear()
                        postAll.clear()
                        for (data in it.post!!) {
                            if (!data.is_deleted) {
                                postAll.add(data)
                                post.add(data)
                            }

                        }
                        adapterPost.notifyDataSetChanged()
                        showProgress(false)

                    } else {
                        toast(it.message.toString())
                        showProgress(false)
                    }
                })
            } else {
                s.let {
                    toast(it)
                }
                showProgress(false)
            }
        })
    }

    private fun showProgress(show: Boolean){
        history_progress.visibility = if(show) View.VISIBLE else View.GONE
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