package com.example.lostandfoundipb.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.lostandfoundipb.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class RegisterActivity : AppCompatActivity() {
    lateinit var name: String
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var confirmPassword: String
    lateinit var telephone: String
    lateinit var role: String
    lateinit var nim: String
    lateinit var faculty: String
    lateinit var department: String
    lateinit var batch: String
    lateinit var nip: String
    lateinit var unit: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        onClick()
    }

    private fun onClick(){
        register_tv_login.setOnClickListener {
            startActivity<RegisterActivity>()
        }
        register_btn_register.setOnClickListener{
            checkRegister()
        }
    }

    private fun checkRegister() {
//        register_name.error = null
//        register_username.error = null
//        register_email.error = null
//        register_password.error = null
//        register_confirm_password.error = null

        val materialButtonToggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.register_groupbtn_role)
        val buttonId = materialButtonToggleGroup.checkedButtonId

        when (buttonId) {
            1 -> {
                role = "student"
            }
            2 -> {
                role = "lecturer"
            }
            3 -> {
                role = "staff"
            }
        }

        toast(role)

    }
}