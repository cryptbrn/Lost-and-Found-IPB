package com.example.lostandfoundipb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.ui.MainActivity
import com.example.lostandfoundipb.ui.viewmodel.ProfileViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

class ProfileFragment : Fragment() {

    lateinit var session: SessionManagement

    lateinit var profileImg: CircleImageView
    lateinit var profileName: TextView
    lateinit var profileEmail: TextView
    lateinit var profileLost: TextView
    lateinit var profileFound: TextView
    lateinit var userData: HashMap<String, String>
    lateinit var logoutBotton: Button
    var lost:Int = 0
    var found:Int = 0



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
        return view
    }

    private fun init(view: View?) {
        profileImg = view!!.profile_iv_user
        profileName = view.profile_tv_name
        profileEmail = view.profile_tv_mail
        profileLost = view.profile_lost
        profileFound = view.profile_found
        logoutBotton = view.profile_btn_sign_out
        onClick()
        setView()
    }


    private fun setView() {
        if (activity != null) userData = (activity as MainActivity).getUserData()
        if (userData["picture"].toString() != "") setImage(userData["picture"].toString())
        profileName.text = userData["name"]
        profileEmail.text = userData["email"]
        profileFound.text = found.toString()
        profileLost.text = lost.toString()
    }

    private fun setImage(url: String) {
        Glide.with(requireActivity())
            .load(url)
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
    }

    private fun logout(){
        showProgress(true)
        viewModel.logout(apiService!!)
        viewModel.logoutString.observe({lifecycle},{s ->
            if(s == "Success"){
                viewModel.logoutResult.observe({lifecycle},{
                    if(it.success){
                        showProgress(false)
                        session.clearSession()
                        session.logout()
                        (activity as MainActivity).finish()
                    }
                    else{
                        toast(it.message)
                        showProgress(false)
                    }
                })
            }
            else{
                s.let {
                    toast(it)

                }
                showProgress(false)
            }
        })

    }

    private fun showProgress(show: Boolean){
        profile_progress.visibility = if(show) View.VISIBLE else View.GONE
        if(activity!=null) (activity as MainActivity).disableTouch(show)
    }
}