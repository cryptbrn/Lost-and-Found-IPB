package com.example.lostandfoundipb.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


class FoundFragment : Fragment(){
    lateinit var progress: LinearLayout
    lateinit var adapterPost: PostAdapter
    lateinit var foundRv: RecyclerView
    private var found: MutableList<Post.Post> = mutableListOf()
    private var foundAll: MutableList<Post.Post> = mutableListOf()
    private var post: MutableList<Post.Post> = mutableListOf()
    lateinit var foundSv: SearchView
    lateinit var session: SessionManagement
    lateinit var createPost: FloatingActionButton
    private lateinit var btnFilter: ImageView
    private lateinit var tvCategory: TextView
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
        val view = inflater.inflate(R.layout.fragment_found, container, false)
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
        activity!!.title = getString(R.string.found)
        createPost = view.found_fab_create_post
        foundSv = view.found_sv
        progress = view.found_progress
        foundRv = view.found_rv
        btnFilter = view.found_filter
        tvCategory = view.found_tv_category
        refresh = view.findViewById(R.id.found_swipe_refresh)
        adapterPost = PostAdapter(found, this.context!!)
        foundRv.adapter = adapterPost
        foundRv.layoutManager = LinearLayoutManager(context)
        search()
        refresh.onRefresh {
            refresh.isRefreshing = false
            getPost()
            tvCategory.visibility= View.GONE
        }

    }

    private fun onClick(){
        createPost.setOnClickListener {
            startActivity<CreatePostActivity>("type" to 1)
        }
        btnFilter.setOnClickListener { showDialogFilter() }
    }


    private fun search() {
        foundSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showProgress(true)
                found.clear()
                for (data in foundAll) {
                    if (data.title.toLowerCase().contains(query.toString()) ||
                        data.description.toLowerCase().contains(query.toString()) ||
                        data.item.name.toLowerCase().contains(query.toString())
                    ) {
                        found.add(data)
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
        ) { _, _ ->

        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateStatus(checkedCategory: BooleanArray, categoryList: List<String>) {
        found.clear()
        lateinit var category: String
        var j = 0
        for (i in checkedCategory.indices){
            val checked = checkedCategory[i]
            if(checked){
                j++
                for(data in foundAll){
                    if(data.item.category.toLowerCase().contains(categoryList[i].toLowerCase())){
                        found.add(data)
                    }
                }
                category = if(j==1){
                    categoryList[i]
                } else{
                    category + ", " + categoryList[i]
                }

            }
        }
        tvCategory.visibility= View.VISIBLE
        tvCategory.text = "Filtered by: $category"
        adapterPost.notifyDataSetChanged()
    }

    private fun getPost(){
        showProgress(true)
        viewModel.getPost(apiService!!)
    }



    private fun postResult(){
        viewModel.postResult.observe({ lifecycle }, { result ->
            if (result.success) {
                found.clear()
                foundAll.clear()
                post.clear()
                post.addAll(result.post!!)
                post.sortWith { c1, c2 -> c2.updated_at.compareTo(c1.updated_at) }
                for (data in post) {
                    if (!data.is_deleted) {
                        if (data.type) {
                            foundAll.add(data)
                            found.add(data)
                            Log.d("FoundFragment", data.toString())
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