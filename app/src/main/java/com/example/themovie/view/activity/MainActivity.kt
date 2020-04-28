package com.example.themovie.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.themovie.R
import com.example.themovie.view.fragment.FavouritesFragment
import com.example.themovie.view.fragment.MovieListFragment
import com.example.themovie.view.fragment.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {


    //test commit
    val fm: FragmentManager? = supportFragmentManager
    var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = fm?.findFragmentById(R.id.fragment_container)

        FirebaseMessaging.getInstance().subscribeToTopic("movies")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Not subscribed"
                }
                Log.d("TAGGG", msg)
            }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bot)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    fragment = MovieListFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment as MovieListFragment)
                        ?.addToBackStack("firstFrag")
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_home).isChecked = true
                }
                R.id.nav_fav -> {
                    fragment = FavouritesFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment as FavouritesFragment)
                        ?.addToBackStack("Second")
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_fav).isChecked = true
                    Log.d("lol", fm?.backStackEntryCount.toString())

                }
                R.id.nav_profile -> {
                    fragment = UserFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment as UserFragment)
                        ?.addToBackStack("Third")
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_profile).isChecked = true

                }
            }
            false
        }
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId =
                R.id.nav_home
        }
    }
//    override fun onBackPressed() {
//        if (fragment != null && fragment?.childFragmentManager?.backStackEntryCount!! > 0) {
//            fragment?.childFragmentManager?.popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }

}
