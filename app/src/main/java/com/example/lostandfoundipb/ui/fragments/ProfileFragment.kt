package com.example.lostandfoundipb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.Utils.Global.Companion.URL_PICT
import com.example.lostandfoundipb.ui.*
import com.example.lostandfoundipb.ui.viewmodel.ChangePasswordViewModel
import com.example.lostandfoundipb.ui.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_change_password.view.*
import kotlinx.android.synthetic.main.activity_deactivate_account.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

class ProfileFragment : Fragment() {

    lateinit var session: SessionManagement

    lateinit var profileImg: ImageView
    lateinit var profileName: TextView
    lateinit var profileUsername: TextView
    lateinit var profileEmail: TextView
    lateinit var profilePhone: TextView
    lateinit var profileRole: TextView
    lateinit var userData: HashMap<String, String>
    lateinit var logoutBotton: TextView
    lateinit var editProfile: TextView
    lateinit var postHistory: TextView
    lateinit var aboutApplication: TextView
    lateinit var changePassword: TextView
    lateinit var deactivateAccount: TextView



    private lateinit var viewModel: ProfileViewModel
    private val apiService by lazy {
        context?.let { ApiService.create(it) }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        session = SessionManagement(requireContext())
        requireActivity().setTitle(getString(R.string.profile))
        init(view)
        logoutResult()
        return view
    }

    private fun init(view: View?) {
        profileImg = view!!.profile_iv_user
        profileName = view.profile_tv_name
        profileUsername = view.profile_tv_username
        profileEmail = view.profile_tv_mail
        profilePhone = view.profile_tv_phone
        profileRole = view.profile_tv_role
        logoutBotton = view.tv_logout
        editProfile = view.tv_edit
        postHistory = view.profile_tv_history
        aboutApplication = view.profile_tv_about
        changePassword = view.tv_edit_password
        deactivateAccount = view.tv_deactivation
        onClick()
        setView()
    }


    private fun setView() {
        if (activity != null) userData = (activity as MainActivity).getUserData()
        if (userData["picture"].toString() != "") setImage(userData["picture"].toString())
        profileName.text = userData["name"]
        profileEmail.text = userData["email"]
        profileUsername.text = userData["username"]!!.capitalize()
        profileRole.text = userData["role"]
        profilePhone.text = userData["telephone"]
    }

    private fun setImage(url: String) {
        Glide.with(requireActivity())
            .load(URL_PICT+url)
            .apply(RequestOptions().placeholder(R.drawable.ic_account_circle))
            .apply(RequestOptions().circleCrop())
            .into(profileImg)
    }


    private fun onClick() {
        logoutBotton.setOnClickListener{
            alert (getString(R.string.logout_confirmation)){
                yesButton {
                    logout()
                }
                noButton {}
            }.show()

        }
        editProfile.setOnClickListener { startActivity<EditProfileActivity>() }
        postHistory.setOnClickListener { startActivity<HistoryPostActivity>() }
        aboutApplication.setOnClickListener { startActivity<AboutApplicationActivity>() }
        changePassword.setOnClickListener { startActivity<ChangePasswordActivity>() }
        deactivateAccount.setOnClickListener { startActivity<DeactivateAccountActivity>() }
    }

    private fun logout(){
        showProgress(true)
        viewModel.logout(apiService!!)
    }
    private fun logoutResult(){
        viewModel.logoutResult.observe({lifecycle},{result ->
            if(result.success){
                showProgress(false)
                session.clearSession()
                session.logout()
                (activity as MainActivity).finish()
            }
            else{
                toast(result.message)
                showProgress(false)
            }
        })
    }

    private fun showProgress(show: Boolean){
        profile_progress.visibility = if(show) View.VISIBLE else View.GONE
        if(activity!=null) (activity as MainActivity).disableTouch(show)
    }
}