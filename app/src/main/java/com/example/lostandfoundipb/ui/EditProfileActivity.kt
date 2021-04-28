package com.example.lostandfoundipb.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.Utils.telephoneValidator
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.retrofit.Global.Companion.URL_PICT
import com.example.lostandfoundipb.retrofit.models.User
import com.example.lostandfoundipb.ui.viewmodel.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {
    private var picture: File = File("")
    private val sizeKb = 1024.0F
    private val sizeMb = sizeKb * sizeKb

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

    companion object {
        private val REQUEST_IMAGE_CAPTURE = 1
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
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
        if(!session.user["picture"].isNullOrBlank()) setImage(session.user["picture"].toString())
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
    private fun setImage(url: String) {
        Glide.with(this)
                .load(URL_PICT +url)
                .apply(RequestOptions().placeholder(R.drawable.ic_account_circle))
                .apply(RequestOptions().circleCrop())
                .into(edit_iv_user)
    }

    private fun setImageNew(imageBitmap: Bitmap) {
        Glide.with(this)
                .asBitmap()
                .load(imageBitmap)
                .apply(RequestOptions().placeholder(R.drawable.ic_account_circle))
                .apply(RequestOptions().circleCrop())
                .into(edit_iv_user)
    }

    private fun onClick() {
        edit_profile_btn_save.setOnClickListener{
            checkEdit()
        }
        edit_profile_tv_picture.setOnClickListener {
            chooseMethod()
        }
    }

    private fun chooseMethod() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.select_method))
        val pictureDialogItems = arrayOf(getString(R.string.from_gallery), getString(R.string.from_camera))
        pictureDialog.setItems(pictureDialogItems)
        { _, which ->
            when (which) {
                0 -> selectFromGallery()
                1 -> takeFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun takeFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun selectFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            picture = createTempFile(imageBitmap)
            setImageNew(imageBitmap)
        }

        else if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == RESULT_OK) {
            val uri = data!!.data
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                picture = createTempFile(imageBitmap)
                setImageNew(imageBitmap)
            } catch (excp: IOException) {
                excp.printStackTrace()
                println(excp.toString())
            }
        }
    }

    private fun createTempFile(bitmap: Bitmap): File {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), session.user["id"] +"_" + System.currentTimeMillis().toString() + ".JPEG")
        var compressionConstant = 100
        var bitmapData = getByteArray(bitmap, compressionConstant)
        while ((bitmapData.size) > 500000) {
            when {
                compressionConstant > 50 -> compressionConstant -= 15
                compressionConstant > 25 -> compressionConstant -= 10
                compressionConstant > 10 -> compressionConstant -= 5
                else -> compressionConstant--
            }
            bitmapData = getByteArray(bitmap, compressionConstant)
        }

        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun getByteArray(bitmap: Bitmap, compressConstant: Int): ByteArray {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressConstant, bos)
        return bos.toByteArray()
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
            val map: HashMap<String, RequestBody> = HashMap()
            map["id"] = createPart(session.user["id"].toString())
            map["name"] = createPart(name)
            map["username"] = createPart(username)
            map["telephone"] = createPart(telephone)
            when {
                session.user["role"]=="student" -> {
                    map["student[nim]"] = createPart(nim)
                    map["student[department]"] = createPart(department)
                    map["student[faculty]"] = createPart(faculty)
                    map["student[batch]"] = createPart(batch)
                }
                session.user["role"]=="lecturer" -> {
                    map["lecturer[nip]"] = createPart(nip)
                    map["lecturer[department]"] = createPart(department)
                    map["lecturer[faculty]"] = createPart(faculty)
                }
                session.user["role"]=="staff" -> {
                    map["staff[nip]"] = createPart(nip)
                    map["staff[unit]"] = createPart(unit)
                }
            }
            attemptEdit(map)
        }

    }

    private fun createPart(value: String): RequestBody {
        return RequestBody.create(
                MultipartBody.FORM, value
        )
    }

    private fun attemptEdit(form: HashMap<String, RequestBody>) {
        showProgress(true)
        val file = RequestBody.create(MediaType.parse("image/jpeg"), picture)
        val pictureReq = MultipartBody.Part.createFormData("picture", picture.name, file)
        if(pictureReq.body().contentLength()>0){
            viewModel.editProfile(apiService,pictureReq,form)
        }
        else{
            viewModel.editProfile(apiService, form)
        }
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