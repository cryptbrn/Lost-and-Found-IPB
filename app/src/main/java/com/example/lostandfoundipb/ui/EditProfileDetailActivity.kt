package com.example.lostandfoundipb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.Utils.telephoneValidator
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.Lecturer
import com.example.lostandfoundipb.retrofit.models.Staff
import com.example.lostandfoundipb.retrofit.models.Student
import com.example.lostandfoundipb.retrofit.models.User
import com.example.lostandfoundipb.ui.viewmodel.EditProfileViewModel
import com.example.lostandfoundipb.ui.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_edit_profile_detail.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class EditProfileDetailActivity : AppCompatActivity() {
    lateinit var name: String
    lateinit var username: String
    lateinit var telephone: String
    lateinit var nim: String
    lateinit var faculty: String
    lateinit var department: String
    lateinit var batch: String
    lateinit var nip: String
    lateinit var unit: String
    lateinit var session: SessionManagement
    lateinit var edit: User.Update
    lateinit var viewModel: EditProfileViewModel



    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_detail)
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)
        supportActionBar?.title = getString(R.string.edit_profile_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        session = SessionManagement(this)
        setView()
        setData()
        onClick()
    }

    private fun setView() {
        when {
            session.user["role"]=="student" -> {
                edit_profile_tv_nim.visibility = View.VISIBLE
                edit_profile_layout_nim.visibility = View.VISIBLE
                edit_profile_tv_department.visibility = View.VISIBLE
                edit_profile_layout_department.visibility = View.VISIBLE
                edit_profile_tv_faculty.visibility = View.VISIBLE
                edit_profile_layout_faculty.visibility = View.VISIBLE
                edit_profile_tv_batch.visibility = View.VISIBLE
                edit_profile_layout_batch.visibility = View.VISIBLE
            }
            session.user["role"]=="lecturer" -> {
                edit_profile_tv_nip.visibility = View.VISIBLE
                edit_profile_layout_nip.visibility = View.VISIBLE
                edit_profile_tv_department.visibility = View.VISIBLE
                edit_profile_layout_department.visibility = View.VISIBLE
                edit_profile_tv_faculty.visibility = View.VISIBLE
                edit_profile_layout_faculty.visibility = View.VISIBLE
            }
            session.user["role"]=="staff" -> {
                edit_profile_tv_nip.visibility = View.VISIBLE
                edit_profile_layout_nip.visibility = View.VISIBLE
                edit_profile_tv_unit.visibility = View.VISIBLE
                edit_profile_layout_unit.visibility = View.VISIBLE
            }
        }
    }

    private fun setData() {
        edit_profile_name.setText(session.user["name"])
        edit_profile_username.setText(session.user["username"])
        edit_profile_telephone.setText(session.user["telephone"])
        edit_profile_nim.setText(session.user["nim"])
        edit_profile_faculty.setText(session.user["faculty"])
        edit_profile_department.setText(session.user["department"])
        edit_profile_batch.setText(session.user["batch"])
        edit_profile_nip.setText(session.user["nip"])
        edit_profile_unit.setText(session.user["unit"])
    }

    private fun onClick() {
        edit_profile_btn_save.setOnClickListener{
            checkEdit()
        }
    }

    private fun checkEdit() {

            edit_profile_telephone.error = null

            var cancel = false
            var focusView: View? = null

            name = edit_profile_name.text.toString()
            username = edit_profile_username.text.toString()
            telephone = edit_profile_telephone.text.toString()
            nim = edit_profile_nim.text.toString()
            nip = edit_profile_nip.text.toString()
            faculty = edit_profile_faculty.text.toString()
            department = edit_profile_department.text.toString()
            batch = edit_profile_batch.text.toString()
            unit = edit_profile_unit.text.toString()

        if (!telephoneValidator(telephone)) {
            register_telephone.error = getString(R.string.phone_format_error)
            focusView = register_telephone
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            when {
                session.user["role"]=="student" -> {
                    val student = Student.Student(nim,faculty,department,batch.toInt())
                    edit = User.Update(session.user["id"]!!.toInt(),name,username,telephone,null,null,student)
                }
                session.user["role"]=="lecturer" -> {
                    val lecturer = Lecturer.Lecturer(nip,faculty,department)
                    edit = User.Update(session.user["id"]!!.toInt(),name,username,telephone,null,lecturer,null)
                }
                session.user["role"]=="staff" -> {
                    val staff = Staff.Staff(nip,unit)
                    edit = User.Update(session.user["id"]!!.toInt(),name,username,telephone,staff,null,null)
                }
            }
            attemptEdit(edit)
        }

    }

    private fun attemptEdit(edit: User.Update) {
        showProgress(true)
        viewModel.editProfile(apiService, edit)
        viewModel.editDetailString.observe({lifecycle},{s ->
            if(s == "Success"){
                viewModel.editDetailResult.observe({lifecycle},{
                    if(it.success){
                        auth()
                        showProgress(false)
                    }
                    else{
                        alert(it.message){
                            yesButton {  }
                        }.show()
                        showProgress(false)
                    }
                })
            }
            else{
                s.let {
                    alert(it){
                        yesButton {  }
                    }.show()
                }
                showProgress(false)
            }
        })
    }

    private fun auth() {
        showProgress(true)
        viewModel.auth(apiService)
        viewModel.authString.observe({lifecycle},{s ->
            if(s == "Success"){
                viewModel.authResult.observe({lifecycle},{
                    if(it.success){
                        session.createSession(it.user)
                        showProgress(false)
                        startActivity<MainActivity>("goto" to "profile")
                        finish()
                    }
                    else{
                        alert(it.message){
                            yesButton {  }
                        }.show()
                        showProgress(false)
                    }
                })
            }
            else{
                s.let {
                    alert(it){
                        yesButton {  }
                    }.show()
                    showProgress(false)
                }
            }
        })
    }

    private fun showProgress(show: Boolean){
        edit_profile_detail_progress.visibility = if(show) View.VISIBLE else View.GONE
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}