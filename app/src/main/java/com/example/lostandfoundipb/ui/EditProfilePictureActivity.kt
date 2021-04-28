package com.example.lostandfoundipb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.ui.viewmodel.EditProfileViewModel
import java.io.File

class EditProfilePictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_picture)
    }
}