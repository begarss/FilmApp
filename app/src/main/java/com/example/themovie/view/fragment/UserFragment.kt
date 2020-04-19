package com.example.themovie.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.themovie.R
import com.example.themovie.model.authorization.LoginActivity
import com.example.themovie.model.authorization.LoginSharedPref

class UserFragment : Fragment() {
    private lateinit var logOut:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_user, container, false)
        logOut = view?.findViewById(R.id.logOut)!!
        logOut.setOnClickListener(View.OnClickListener {
            LoginSharedPref().clearUserName(requireActivity())
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        })

        return view
    }
}
