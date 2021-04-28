package com.example.lostandfoundipb.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.lostandfoundipb.R
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.jetbrains.anko.startActivity


class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.title = getString(R.string.edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        onClick()
    }

    private fun onClick() {
        tv_edit_detail.setOnClickListener { startActivity<EditProfileDetailActivity>() }
        tv_edit_picture.setOnClickListener { startActivity<EditProfilePictureActivity>() }
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