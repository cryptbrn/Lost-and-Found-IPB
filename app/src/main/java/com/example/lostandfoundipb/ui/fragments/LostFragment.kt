package com.example.lostandfoundipb.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_found.view.*
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
    lateinit var btnFilter: ImageView
    lateinit var tvCategory: TextView
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
        val view = inflater.inflate(R.layout.fragment_lost, container, false)
        session = SessionManagement(requireContext())
        viewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)
        init(view)
        onClick()
        getPost()
        postResult()
        return view
    }


    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        activity!!.title = getString(R.string.lost)
        createPost = view.lost_fab_create_post
        lostSv = view.lost_sv
        progress = view.lost_progress
        lostRv = view.lost_rv
        btnFilter = view.lost_filter
        tvCategory = view.lost_tv_category
        refresh = view.findViewById(R.id.lost_swipe_refresh)
        adapterPost = PostAdapter(lost, this.context!!)
        lostRv.adapter = adapterPost
        lostRv.layoutManager = LinearLayoutManager(context)
        search()
        refresh.onRefresh {
            refresh.isRefreshing = false
            getPost()
            tvCategory.visibility = View.GONE
        }

    }

    private fun onClick(){
        createPost.setOnClickListener {
            startActivity<CreatePostActivity>("type" to 2)
        }
        btnFilter.setOnClickListener { showDialogFilter() }
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

    private fun showDialogFilter() {
        val alertDialog = AlertDialog.Builder(activity as MainActivity)
        alertDialog.setTitle(getString(R.string.choose_status))
        val category = arrayOf(
                getString(R.string.important_documents),
                getString(R.string.gadgets_amp_electronics),
                getString(R.string.lunch_box_amp_bottles),
                getString(R.string.clothes_amp_accessories),
                getString(R.string.stationary_amp_module_book),
                getString(R.string.others)
        )

        val checkedCategory = booleanArrayOf(
                false,
                false,
                false,
                false,
                false,
                false
        )

        val categoryList: List<String> = category.toList()

        alertDialog.setMultiChoiceItems(category, checkedCategory){ _, which, isChecked ->
            checkedCategory[which] = isChecked
        }
        alertDialog.setPositiveButton(
                "Show"
        ) { _, _ ->
            updateStatus(checkedCategory, categoryList)
        }
        alertDialog.setNegativeButton(
                "Cancel"
        ){ _, _ ->

        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateStatus(checkedCategory: BooleanArray, categoryList: List<String>) {
        lost.clear()
        lateinit var category: String
        var j = 0
        for(i in checkedCategory.indices) {
            val checked = checkedCategory[i]
            if(checked) {
                j++
                for(data in lostAll) {
                    if(data.item.category.toLowerCase().contains(categoryList[i].toLowerCase())) {
                        lost.add(data)
                    }
                }
                category = if(j==1) {
                    categoryList[i]
                } else {
                    category + ", " + categoryList[i]
                }
            }
        }
        tvCategory.visibility = View.VISIBLE
        tvCategory.text = "Filtered by: $category"
        adapterPost.notifyDataSetChanged()
    }

    private fun getPost(){
        showProgress(true)
        viewModel.getPost(apiService!!)
    }

    private fun postResult(){
        viewModel.postResult.observe({ lifecycle },{ result ->
            if (result.success) {
                lost.clear()
                lostAll.clear()
                post.clear()
                post.addAll(result.post!!)
                post.sortWith { c1, c2 -> c2.updated_at.compareTo(c1.updated_at) }
                for (data in post) {
                    if (!data.is_deleted) {
                        if (!data.type) {
                            lostAll.add(data)
                            lost.add(data)
                        }
                    }

                }
                adapterPost.notifyDataSetChanged()
                showProgress(false)

            } else {
                toast(result.message.toString())
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