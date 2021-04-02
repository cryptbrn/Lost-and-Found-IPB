package com.example.lostandfoundipb.ui



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.ui.fragments.FoundFragment
import com.example.lostandfoundipb.ui.fragments.HomeFragment
import com.example.lostandfoundipb.ui.fragments.ProfileFragment
import com.example.lostandfoundipb.ui.fragments.LostFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val homeFragment: Fragment = HomeFragment()
    private val foundFragment: Fragment = FoundFragment()
    private val lostFragment: Fragment = LostFragment()
    private val profileFragment: Fragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}