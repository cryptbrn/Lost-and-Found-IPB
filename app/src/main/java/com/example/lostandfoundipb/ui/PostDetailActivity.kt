package com.example.lostandfoundipb.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.Utils.formatDate
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.Global.Companion.URL_PICT
import com.example.lostandfoundipb.retrofit.Global.Companion.URL_POST
import com.example.lostandfoundipb.retrofit.models.Post
import com.example.lostandfoundipb.retrofit.models.User
import com.example.lostandfoundipb.ui.viewmodel.PostDetailViewModel
import kotlinx.android.synthetic.main.activity_post_detail.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class PostDetailActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create(this)
    }
    private lateinit var post: Post.Post
    private lateinit var viewModel: PostDetailViewModel
    lateinit var session: SessionManagement
    lateinit var user: User.User


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        post = intent.getParcelableExtra("post")!!
        viewModel = ViewModelProviders.of(this).get(PostDetailViewModel::class.java)
        session = SessionManagement(this)
        supportActionBar?.title = post.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setData()
        onClick()
        checkUser()
    }

    private fun checkUser() {
        if(post.user_id.toString()==session.user["id"]){
            detail_person_tv.visibility = GONE
            detail_person_layout.visibility = GONE
            detail_btn_left.text = getString(R.string.edit_post)
            detail_btn_right.text = getString(R.string.delete_post)
            detail_btn_right.setPadding(0, 0, 0, 0)
            detail_btn_left.setPadding(0, 0, 0, 0)
            detail_btn_right.compoundDrawablePadding = 0
            detail_btn_left.compoundDrawablePadding = 0
            detail_btn_left.setCompoundDrawables(null, null, null, null)
            detail_btn_right.setCompoundDrawables(null, null, null, null)
            detail_btn_right.setBackgroundResource(R.drawable.rounded_red)
        }
        else {
            getPersonData(post.user_id.toString())
        }
    }

    private fun getPersonData(id: String){
        showProgress(true)
        viewModel.getPerson(apiService, id)
        viewModel.personString.observe({ lifecycle }, { s ->
            if (s == "Success") {
                viewModel.personResult.observe({ lifecycle }, {
                    if (it.success) {
                        setPerson(it.user)
                        user = it.user
                        showProgress(false)
                    } else {
                        toast(it.message)
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

    private fun setPerson(person: User.User) {
        detail_person_name.text = person.name
        person.picture.let { setImagePerson(it) }

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

    private fun onClick() {
        if(post.user_id.toString()==session.user["id"]){
            detail_btn_right.setOnClickListener {
                Log.d("MASUK", "MASUK NIH")
                alert("Are you sure want to delete this post") {
                    positiveButton("Yes") { deletePost() }
                    negativeButton("No") { }
                }.show()
            }
            detail_btn_left.setOnClickListener { chooseAction() }
        }
        else{
            detail_btn_right.setOnClickListener { openWhatsapp() }
            detail_btn_left.setOnClickListener{ openDialer() }
        }

    }

    private fun openDialer() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${user.telephone}")
        startActivity(intent)

    }

    private fun openWhatsapp() {
        val url = "https://api.whatsapp.com/send?phone=${"+62"+user.telephone.substring(1)}"
        try{
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        catch (e: PackageManager.NameNotFoundException){
            toast("Whatsapp is not installed in your phone.")
            e.printStackTrace()
        }
    }

    private fun chooseAction() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.choose_action))
        val pictureDialogItems = arrayOf(
            getString(R.string.edit_post_status),
            getString(R.string.edit_post_detail)
        )
        pictureDialog.setItems(pictureDialogItems)
        { _, which ->
            when (which) {
                0 -> showDialogStatus()
                1 -> startActivity<EditPostActivity>("post" to post)
            }
        }
        pictureDialog.show()
    }

    private fun showDialogStatus() {
        lateinit var checked:String
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.choose_status))
        if(post.type){
            val items = arrayOf(
                getString(R.string.item_at_discoverer), getString(R.string.item_at_ukk), getString(
                    R.string.item_claimed
                )
            )
            val checkedItem = post.status
            alertDialog.setSingleChoiceItems(items, checkedItem) { _, which ->
                when (which) {
                    0 -> checked = 0.toString()
                    1 -> checked = 1.toString()
                    2 -> checked = 2.toString()
                }
            }
        }
        else {
            val items = arrayOf(
                getString(R.string.item_still_not_found),
                getString(R.string.item_found)
            )
            val checkedItem = post.status
            alertDialog.setSingleChoiceItems(items, checkedItem) { _, which ->
                when (which) {
                    0 -> checked = 0.toString()
                    1 -> checked = 1.toString()
                }
            }
        }
        alertDialog.setPositiveButton("Submit"
        ) { _, _ ->
            updateStatus(checked)
        }
        alertDialog.setNegativeButton("Cancel"
        ) { _, _ ->

        }

        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }


    private fun updateStatus(type: String) {
        showProgress(true)
        val map: HashMap<String, RequestBody> = HashMap()
        map["type"] = RequestBody.create(MultipartBody.FORM, type)
        viewModel.editPost(apiService, map, post.id.toString())
        viewModel.editPostString.observe({ lifecycle }, { s ->
            if (s == "Success") {
                viewModel.editPostResult.observe({ lifecycle }, {
                    if (it.success) {
                        if (post.type) {
                            when (type.toInt()) {
                                0 -> {
                                    detail_status_btn.text = getString(R.string.at_discoverer)
                                    detail_status_btn.setBackgroundResource(R.drawable.rounded_red)
                                }
                                1 -> {
                                    detail_status_btn.text = getString(R.string.at_ukk)
                                    detail_status_btn.setBackgroundResource(R.drawable.rounded_darkblue)

                                }
                                else -> {
                                    detail_status_btn.text = getString(R.string.claimed)
                                    detail_status_btn.setBackgroundResource(R.drawable.rounded_green)

                                }
                            }
                        } else {
                            when (type.toInt()) {
                                0 -> {
                                    detail_status_btn.text = getString(R.string.item_not_found)
                                    detail_status_btn.setBackgroundResource(R.drawable.rounded_red)
                                }
                                else -> {
                                    detail_status_btn.text = getString(R.string.item_found)
                                    detail_status_btn.setBackgroundResource(R.drawable.rounded_green)

                                }
                            }
                        }
                        showProgress(false)
                    } else {
                        toast(it.message)
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


    private fun deletePost() {
        showProgress(true)
        viewModel.deletePost(apiService, post.id.toString())
        viewModel.deleteString.observe({ lifecycle }, { s ->
            if (s == "Success") {
                viewModel.deleteResult.observe({ lifecycle }, {
                    if (it.success) {
                        startActivity<MainActivity>("goto" to "home")
                        finish()
                        showProgress(false)
                    } else {
                        toast(it.message)
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

    @SuppressLint("SetTextI18n")
    private fun setData() {
        if(post.type){
            detail_person_tv.text = getString(R.string.discoverer)
            detail_location_tv.text = getString(R.string.item_found_location)
            when (post.status) {
                0 -> {
                    detail_status_btn.text = getString(R.string.at_discoverer)
                    detail_status_btn.setBackgroundResource(R.drawable.rounded_red)
                }
                1 -> {
                    detail_status_btn.text = getString(R.string.at_ukk)
                    detail_status_btn.setBackgroundResource(R.drawable.rounded_darkblue)

                }
                else -> {
                    detail_status_btn.text = getString(R.string.claimed)
                    detail_status_btn.setBackgroundResource(R.drawable.rounded_green)

                }
            }
        }
        else {
            detail_person_tv.text = getString(R.string.searcher)
            detail_location_tv.text = getString(R.string.item_last_location)
            when (post.status) {
                0 -> {
                    detail_status_btn.text = getString(R.string.item_not_found)
                    detail_status_btn.setBackgroundResource(R.drawable.rounded_red)
                }
                else -> {
                    detail_status_btn.text = getString(R.string.item_found)
                    detail_status_btn.setBackgroundResource(R.drawable.rounded_green)

                }
            }
        }
        detail_title.text = post.title
        detail_category.text = getString(R.string.category_1)+ post.item.category
        detail_location.text = post.item.location
        detail_description.text = post.description
        detail_date.text = formatDate(post.item.updated_at, this)
        post.item.picture.let { setImage(it) }
    }



    private fun setImage(url: String) {
        Glide.with(this)
                .load(URL_POST + url)
                .apply(RequestOptions().centerCrop())
                .into(detail_picture)
    }

    private fun setImagePerson(url: String){
        Glide.with(this)
                .load(URL_PICT + url)
                .apply(RequestOptions().circleCrop())
                .into(detail_person_picture)
    }


    private fun showProgress(show: Boolean){
        detail_progress.visibility = if(show) View.VISIBLE else GONE
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