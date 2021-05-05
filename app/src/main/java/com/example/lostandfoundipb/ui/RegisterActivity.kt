package com.example.lostandfoundipb.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.emailValidator
import com.example.lostandfoundipb.Utils.passwordValidator
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.android.synthetic.main.activity_register.*
import com.example.lostandfoundipb.Utils.telephoneValidator
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.models.User
import com.example.lostandfoundipb.ui.viewmodel.RegisterViewModel
import okhttp3.RequestBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton


class RegisterActivity : AppCompatActivity() {
    lateinit var name: String
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var confirmPassword: String
    lateinit var telephone: String
    var role: String = ""
    lateinit var nim: String
    lateinit var faculty: String
    lateinit var department: String
    lateinit var batch: String
    lateinit var nip: String
    lateinit var unit: String
    lateinit var register: User.SignUp

    lateinit var viewModel: RegisterViewModel
    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        setGroup()
        registerResult()
        onClick()
    }

    private fun setGroup() {
        val materialButtonToggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.register_groupbtn_role)
        materialButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.register_btn_student) {
                    role = "student"
                    clearView()
                    register_tv_nim.visibility = View.VISIBLE
                    register_layout_nim.visibility = View.VISIBLE
                    register_tv_faculty.visibility = View.VISIBLE
                    register_layout_faculty.visibility = View.VISIBLE
                    register_tv_department.visibility = View.VISIBLE
                    register_layout_department.visibility = View.VISIBLE
                    register_tv_batch.visibility = View.VISIBLE
                    register_layout_batch.visibility = View.VISIBLE
                }
                if (checkedId == R.id.register_btn_lecturer){
                    role = "lecturer"
                    clearView()
                    register_tv_nip.visibility = View.VISIBLE
                    register_layout_nip.visibility = View.VISIBLE
                    register_tv_faculty.visibility = View.VISIBLE
                    register_layout_faculty.visibility = View.VISIBLE
                    register_tv_department.visibility = View.VISIBLE
                    register_layout_department.visibility = View.VISIBLE
                }
                if (checkedId == R.id.register_btn_staff){
                    role = "staff"
                    clearView()
                    register_tv_nip.visibility = View.VISIBLE
                    register_layout_nip.visibility = View.VISIBLE
                    register_tv_unit.visibility = View.VISIBLE
                    register_layout_unit.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun clearView() {
        register_tv_nim.visibility = View.GONE
        register_layout_nim.visibility = View.GONE
        register_tv_nip.visibility = View.GONE
        register_layout_nip.visibility = View.GONE
        register_tv_faculty.visibility = View.GONE
        register_layout_faculty.visibility = View.GONE
        register_tv_department.visibility = View.GONE
        register_layout_department.visibility = View.GONE
        register_tv_batch.visibility = View.GONE
        register_layout_batch.visibility = View.GONE
        register_tv_unit.visibility = View.GONE
        register_layout_unit.visibility = View.GONE
    }

    private fun onClick(){
        register_tv_login.setOnClickListener {
            startActivity<LoginActivity>()
            finish()
        }
        register_btn_register.setOnClickListener{
            checkRegister()
        }
    }

    private fun checkRegister() {
        register_name.error = null
        register_username.error = null
        register_email.error = null
        register_password.error = null
        register_confirm_password.error = null
        register_nim.error = null
        register_nip.error = null
        register_faculty.error = null
        register_department.error = null
        register_unit.error = null
        register_batch.error = null

        var cancel = false
        var focusView: View? = null

        name = register_name.text.toString()
        username = register_username.text.toString()
        email = register_email.text.toString()
        telephone = register_telephone.text.toString()
        password = register_password.text.toString()
        confirmPassword = register_confirm_password.text.toString()
        nim = register_nim.text.toString()
        nip = register_nip.text.toString()
        faculty = register_faculty.text.toString()
        department = register_department.text.toString()
        batch = register_batch.text.toString()
        unit = register_unit.text.toString()

        if (TextUtils.isEmpty(name)) {
            register_name.error = getString(R.string.error_empty)
            focusView = register_name
            cancel = true
        }

        if (TextUtils.isEmpty(username)) {
            register_username.error = getString(R.string.error_empty)
            focusView = register_username
            cancel = true
        }

        if (TextUtils.isEmpty(email)) {
            register_email.error = getString(R.string.error_empty)
            focusView = register_email
            cancel = true
        }
        else if (!TextUtils.isEmpty(email) && !emailValidator(email)) {
            register_email.error = getString(R.string.email_error)
            focusView = register_email
            cancel = true
        }

        if (TextUtils.isEmpty(telephone)) {
            register_telephone.error = getString(R.string.error_empty)
            focusView = register_telephone
            cancel = true
        }
        else if (!telephoneValidator(telephone)) {
            register_telephone.error = getString(R.string.phone_format_error)
            focusView = register_telephone
            cancel = true
        }

        if (TextUtils.isEmpty(password)) {
            register_password.error = getString(R.string.error_empty)
            focusView = register_password
            cancel = true
        }
        else if (!passwordValidator(password)) {
            register_password.error = getString(R.string.password_error)
            focusView = register_password
            cancel = true
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            register_confirm_password.error = getString(R.string.error_empty)
            focusView = register_confirm_password
            cancel = true
        }
        else if (!passwordValidator(confirmPassword)) {
            register_confirm_password.error = getString(R.string.password_error)
            focusView = register_confirm_password
            cancel = true
        }
        else if (confirmPassword != password){
            register_confirm_password.error = getString(R.string.password_not_match)
            register_password.error = getString(R.string.password_not_match)
            focusView = register_confirm_password
            cancel = true
        }

        if(role.isBlank()){
            toast("Please choose a role")
            cancel = true
        }
        else if(role=="student"){
            if (TextUtils.isEmpty(nim)) {
                register_nim.error = getString(R.string.error_empty)
                focusView = register_nim
                cancel = true
            }

            if (TextUtils.isEmpty(faculty)) {
                register_faculty.error = getString(R.string.error_empty)
                focusView = register_faculty
                cancel = true
            }

            if (TextUtils.isEmpty(department)) {
                register_department.error = getString(R.string.error_empty)
                focusView = register_department
                cancel = true
            }

            if (TextUtils.isEmpty(batch)) {
                register_batch.error = getString(R.string.error_empty)
                focusView = register_batch
                cancel = true
            }

        }
        else if (role=="lecturer"){
            if (TextUtils.isEmpty(nip)) {
                register_nip.error = getString(R.string.error_empty)
                focusView = register_nip
                cancel = true
            }

            if (TextUtils.isEmpty(faculty)) {
                register_faculty.error = getString(R.string.error_empty)
                focusView = register_faculty
                cancel = true
            }

            if (TextUtils.isEmpty(department)) {
                register_department.error = getString(R.string.error_empty)
                focusView = register_department
                cancel = true
            }
        }
        else if (role=="staff"){
            if (TextUtils.isEmpty(nip)) {
                register_nip.error = getString(R.string.error_empty)
                focusView = register_nip
                cancel = true
            }

            if (TextUtils.isEmpty(unit)) {
                register_unit.error = getString(R.string.error_empty)
                focusView = register_unit
                cancel = true
            }
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            if(role=="student"){
                register = User.SignUp(name,username,email,telephone,password,role,nim,"",faculty,department,"",batch.toInt())
            }
            else if(role=="lecturer"){
                register = User.SignUp(name,username,email,telephone,password,role,"",nip,faculty,department,"",null)
            }
            else if(role=="staff"){
                register = User.SignUp(name,username,email,telephone,password,role,"",nip,"","",unit,null)
            }
            attemptRegister(register)
        }







    }

    private fun attemptRegister(register: User.SignUp) {
        showProgress(true)
        viewModel.register(apiService, register)
    }

    private fun registerResult(){
        viewModel.registerResult.observe({lifecycle},{result ->
            if(result.success){
                showProgress(false)
                alert(result.message){
                    yesButton {
                        startActivity<LoginActivity>()
                        finish()
                    }
                }.show()
            }
            else{
                alert(result.message){
                    yesButton {  }
                }.show()
                showProgress(false)
            }
        })
    }

    private fun showProgress(show: Boolean){
        register_progress.visibility = if(show) View.VISIBLE else View.GONE
        disableTouch(show)
    }

    fun disableTouch(status: Boolean){
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