package com.example.lostandfoundipb.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.adapters.PostAdapter
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Post
import com.example.lostandfoundipb.ui.CreatePostActivity
import com.example.lostandfoundipb.ui.HistoryPostActivity
import com.example.lostandfoundipb.ui.MainActivity
import com.example.lostandfoundipb.ui.SearchActivity
import com.example.lostandfoundipb.ui.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.dialog_home.*
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
    lateinit var btnLost: Button
    lateinit var btnFound: Button
    lateinit var btnFoundAll: Button
    lateinit var btnLostAll: Button
    private var lost: MutableList<Post.Post> = mutableListOf()
    private var found: MutableList<Post.Post> = mutableListOf()
    lateinit var session: SessionManagement
    private val apiService by lazy {
        context?.let { ApiService.create(it) }
    }
    private lateinit var viewModel : PostViewModel






    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        session = SessionManagement(requireContext())
        viewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)
        init(view)
        getPost()
        onClick()
        return view
    }

    private fun onClick() {
        myPost.setOnClickListener { startActivity<HistoryPostActivity>() }
        btnLost.setOnClickListener { showDialogCategory(getString(R.string.dialog_home_title_lost), false) }
        btnFound.setOnClickListener { showDialogCategory(getString(R.string.dialog_home_title_found), true) }
        btnLostAll.setOnClickListener { startActivity<MainActivity>("goto" to "lost") }
        btnFoundAll.setOnClickListener { startActivity<MainActivity>("goto" to "found") }
    }

    private fun showDialogCategory(title: String, type:Boolean) {
        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        val mView: View = layoutInflater.inflate(R.layout.dialog_home, null)

        val btnDocuments = mView.findViewById<View>(R.id.dialog_documents)
        val btnElectronics = mView.findViewById<View>(R.id.dialog_electronics)
        val btnLunchBox = mView.findViewById<View>(R.id.dialog_lunch_box)
        val btnClothes = mView.findViewById<View>(R.id.dialog_clothes)
        val btnStationary = mView.findViewById<View>(R.id.dialog_stationary)
        val btnOthers = mView.findViewById<View>(R.id.dialog_others)
        val tvTitle = mView.findViewById<TextView>(R.id.dialog_text)


        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        tvTitle.text = title

        btnDocuments.setOnClickListener{
            showActionDialog(getString(R.string.important_documents),type,1)
        }
        btnElectronics.setOnClickListener{
            showActionDialog(getString(R.string.gadgets_amp_electronics),type,2)
        }
        btnLunchBox.setOnClickListener{
            showActionDialog(getString(R.string.lunch_box_amp_bottles),type,3)
        }
        btnClothes.setOnClickListener{
            showActionDialog(getString(R.string.clothes_amp_accessories),type,4)
        }
        btnStationary.setOnClickListener{
            showActionDialog(getString(R.string.stationary_amp_module_book),type,5)
        }
        btnOthers.setOnClickListener{
            showActionDialog(getString(R.string.others),type,6)
        }


    }

    private fun showActionDialog(category: String, type: Boolean, pos: Int) {
        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        val mView: View = layoutInflater.inflate(R.layout.dialog_home_search, null)

        val btnSearch = mView.findViewById<View>(R.id.dialog_search)
        val btnCreateNew = mView.findViewById<View>(R.id.dialog_create)


        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        btnSearch.setOnClickListener{
            startActivity<SearchActivity>("type" to type, "category" to category)
        }

        btnCreateNew.setOnClickListener {
            var mType = 0
            if(type){
                mType=1
            }
            else{
                mType=2
            }
            startActivity<CreatePostActivity>("type" to mType, "category" to pos)
        }




    }

    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        activity!!.title = getString(R.string.app_name)
        progress = view.home_progress
        foundRv = view.home_found_rv
        lostRv = view.home_lost_rv
        btnLost = view.home_btn_lost
        btnFound = view.home_btn_found
        btnFoundAll = view.home_found_all_btn
        btnLostAll = view.home_lost_all_btn
        adapterPostFound = PostAdapter(found, this.context!!)
        foundRv.adapter = adapterPostFound

        adapterPostLost = PostAdapter(lost, this.context!!)
        lostRv.adapter = adapterPostLost

        foundRv.layoutManager = GridLayoutManager(context,3,GridLayoutManager.HORIZONTAL,false)
        lostRv.layoutManager = GridLayoutManager(context,3,GridLayoutManager.HORIZONTAL,false)

        myPost = view.home_btn_my_post

        username = view.home_username
        username.text = getString(R.string.home_hallo) + session.user["name"]
        username.requestFocus()

    }

    private fun getPost(){
        showProgress(true)
        viewModel.getPost(apiService!!)
        viewModel.postString.observe({ lifecycle }, { s ->
            if (s == "Success") {
                viewModel.postResult.observe({ lifecycle }, {
                    if (it.success) {

                        lost.clear()
                        found.clear()
                        for (data in it.post!!) {
                            if (!data.is_deleted) {
                                if (data.status) {
                                    if (found.size < 3) {
                                        found.add(data)
                                    }
                                } else {
                                    if (lost.size < 3) {
                                        lost.add(data)
                                    }
                                }
                            }

                        }
                        if (found.size < 1) {
                            home_recently_found.visibility = View.GONE
                        }
                        if (lost.size < 1) {
                            home_recently_lost.visibility = View.GONE
                        }
                        adapterPostLost.notifyDataSetChanged()
                        adapterPostFound.notifyDataSetChanged()
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
        progress.bringToFront()
        progress.visibility = if(show) View.VISIBLE else View.GONE
        if(activity!=null) (activity as MainActivity).disableTouch(show)
    }
}