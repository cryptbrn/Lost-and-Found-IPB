package com.example.lostandfoundipb.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.adapters.PostAdapter
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Post
import com.example.lostandfoundipb.ui.CreatePostActivity
import com.example.lostandfoundipb.ui.MainActivity
import com.example.lostandfoundipb.ui.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_lost.view.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class LostFragment : Fragment(){
    lateinit var progress: LinearLayout
    lateinit var adapterPost: PostAdapter
    lateinit var lostRv: RecyclerView
    private var lost: MutableList<Post.Post> = mutableListOf()
    private var lostAll : MutableList<Post.Post> = mutableListOf()
    private var post: MutableList<Post.Post> = mutableListOf()
    lateinit var session: SessionManagement
    lateinit var lostSv: SearchView
    lateinit var createPost: FloatingActionButton
    private lateinit var refresh: SwipeRefreshLayout
    private val apiService by lazy {
        context?.let { ApiService.create(it) }
    }
    private lateinit var viewModel : PostViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lost,container,false)
        session = SessionManagement(requireContext())
        viewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)
        init(view)
        onClick()
        getPost()
        return view
    }


    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        activity!!.title = getString(R.string.lost)
        createPost = view.lost_fab_create_post
        lostSv = view.lost_sv
        progress = view.lost_progress
        lostRv = view.lost_rv
        refresh = view.findViewById(R.id.lost_swipe_refresh)
        adapterPost = PostAdapter(lost, this.context!!)
        lostRv.adapter = adapterPost
        lostRv.layoutManager = LinearLayoutManager(context)
        search()
        refresh.onRefresh {
            refresh.isRefreshing = false
            getPost()
        }

    }

    private fun onClick(){
        createPost.setOnClickListener {
            startActivity<CreatePostActivity>("type" to 2)
        }
    }


    private fun search() {
        lostSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showProgress(true)
                lost.clear()
                for (data in lostAll){
                    if(data.title.toLowerCase().contains(query.toString())||
                        data.description.toLowerCase().contains(query.toString())||
                        data.item.name.toLowerCase().contains(query.toString())){
                        lost.add(data)
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
        viewModel.postString.observe({lifecycle},{s ->
            if(s == "Success"){
                viewModel.postResult.observe({lifecycle},{
                    if(it.success){
                        lost.clear()
                        lostAll.clear()
                        post.clear()
                        post.addAll(it.post!!)
                        post.sortWith{c1, c2 -> c2.updated_at.compareTo(c1.updated_at)}
                        for (data in post){
                            if(!data.is_deleted){
                                if(!data.type){
                                    lostAll.add(data)
                                    lost.add(data)
                                }
                            }

                        }
                        adapterPost.notifyDataSetChanged()
                        showProgress(false)

                    }
                    else{
                        toast(it.message.toString())
                        showProgress(false)
                    }
                })
            }
            else{
                s.let {
                    toast(it)
                }
                showProgress(false)
            }
        })
    }

    private fun showProgress(show: Boolean){
        progress.bringToFront()
        progress.visibility = if(show) View.VISIBLE else View.GONE
        if(activity!=null) (activity as MainActivity).disableTouch(show)
    }
}