package com.example.lostandfoundipb.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.adapters.PostAdapter
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Post
import com.example.lostandfoundipb.ui.EditProfileActivity
import com.example.lostandfoundipb.ui.MainActivity
import com.example.lostandfoundipb.ui.PostDetailActivity
import com.example.lostandfoundipb.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class HomeFragment : Fragment(){
    lateinit var progress: LinearLayout
    lateinit var adapterPostLost: PostAdapter
    lateinit var adapterPostFound: PostAdapter
    lateinit var username: TextView
    lateinit var lostRv: RecyclerView
    lateinit var foundRv: RecyclerView
    lateinit var myPost: Button
    private var lost: MutableList<Post.Post> = mutableListOf()
    private var found: MutableList<Post.Post> = mutableListOf()
    lateinit var session: SessionManagement
    private val apiService by lazy {
        context?.let { ApiService.create(it) }
    }
    private lateinit var viewModel : HomeViewModel






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        session = SessionManagement(requireContext())
        viewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)
        init(view)
        getPost()

        onClick()
        return view
    }

    private fun onClick() {
        myPost.setOnClickListener { }
    }

    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        progress = view.home_progress
        foundRv = view.home_found_rv
        lostRv = view.home_lost_rv

        adapterPostFound = PostAdapter(found, this.context!!)
        foundRv.adapter = adapterPostFound

        adapterPostLost = PostAdapter(lost, this.context!!)
        lostRv.adapter = adapterPostLost

        foundRv.layoutManager = LinearLayoutManager(context)
        lostRv.layoutManager = LinearLayoutManager(context)

        myPost = view.home_btn_my_post

        username = view.home_username
        username.text = getString(R.string.home_hallo) + session.user["name"]
        username.requestFocus()

    }

    private fun getPost(){
        showProgress(true)
        viewModel.getPost(apiService!!)
        viewModel.postString.observe({lifecycle},{s ->
            if(s == "Success"){
                viewModel.postResult.observe({lifecycle},{
                    if(it.success){

                        lost.clear()
                        found.clear()
                        for (data in it.post!!){
                            if(data.status){
                                found.add(data)
                            }
                            else{
                                lost.add(data)
                            }
                        }

                        if(found.size < 1){
                            home_recently_found.visibility = View.GONE
                        }
                        if(lost.size < 1){
                            home_recently_lost.visibility = View.GONE
                        }

                        adapterPostLost.notifyDataSetChanged()
                        adapterPostFound.notifyDataSetChanged()
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