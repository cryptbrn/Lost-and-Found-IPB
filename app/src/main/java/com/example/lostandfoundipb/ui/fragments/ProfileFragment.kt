package com.example.lostandfoundipb.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lostandfoundipb.R
import com.example.lostandfoundipb.Utils.SessionManagement
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    lateinit var session: SessionManagement
    lateinit var test: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        session = SessionManagement(requireContext())
        test = view.test

        Log.d("test name", session.name)
        if(!session.name.isNullOrBlank()){
            test.text = session.name
        }

        return view
    }
}