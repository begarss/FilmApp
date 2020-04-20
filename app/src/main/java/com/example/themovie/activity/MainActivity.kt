package com.example.themovie.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.themovie.R
import com.example.themovie.fragment.FavouritesFragment
import com.example.themovie.fragment.MovieListFragment
import com.example.themovie.fragment.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    val fm: FragmentManager? = supportFragmentManager
    var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = fm?.findFragmentById(R.id.fragment_container)

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

    override fun onBackPressed() {
        if (fm?.backStackEntryCount!! <= 1) {
            super.onBackPressed()
        } else
            fm.popBackStack()
    }

}
