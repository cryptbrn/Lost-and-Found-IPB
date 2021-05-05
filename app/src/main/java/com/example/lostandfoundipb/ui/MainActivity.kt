package com.example.lostandfoundipb.ui



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import com.example.lostandfoundipb.retrofit.ApiService
import com.example.lostandfoundipb.ui.fragments.FoundFragment
import com.example.lostandfoundipb.ui.fragments.HomeFragment
import com.example.lostandfoundipb.ui.fragments.ProfileFragment
import com.example.lostandfoundipb.ui.fragments.LostFragment
import com.example.lostandfoundipb.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.HashMap

class MainActivity : AppCompatActivity() {
    private val homeFragment: Fragment = HomeFragment()
    private val foundFragment: Fragment = FoundFragment()
    private val lostFragment: Fragment = LostFragment()
    private val profileFragment: Fragment = ProfileFragment()
    lateinit var session: SessionManagement
    private lateinit var viewModel: MainViewModel
    private val apiService by lazy {
        this.let { ApiService.create(this) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManagement(this)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        Log.d("test token",session.token)
        authUser()
        authResult()

        navigation.setOnNavigationItemSelectedListener { item ->
            clearFragmentStack()
            when(item.itemId){
                R.id.nav_home    -> { commitFragment(homeFragment) }
                R.id.nav_found   -> { commitFragment(foundFragment) }
                R.id.nav_lost  -> { commitFragment(lostFragment) }
                R.id.nav_profile -> { commitFragment(profileFragment) }
            }
            true
        }
        when{
            intent.getStringExtra("goto") == "found" -> navigation.selectedItemId = R.id.nav_found
            intent.getStringExtra("goto") == "search" -> navigation.selectedItemId = R.id.nav_lost
            intent.getStringExtra("goto") == "profile" -> navigation.selectedItemId = R.id.nav_profile
            else -> navigation.selectedItemId = R.id.nav_home
        }
    }

    private fun commitFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun clearFragmentStack() {
        val fm = this.supportFragmentManager
        for ( i in 0..fm.backStackEntryCount){
            fm.popBackStack()
        }
    }

    private fun authUser(){
        viewModel.auth(apiService)
    }

    private fun authResult(){
        viewModel.authResult.observe({lifecycle},{result ->
            if(result.success){
                session.createSession(result.user!!)
            }
            else{
                toast(result.message!!)
            }
        })
    }



    fun getUserData(): HashMap<String, String> {
        return session.user
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