package com.example.themovie

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.themovie.fragment.FavouritesFragment
import com.example.themovie.fragment.MovieListFragment
import com.example.themovie.fragment.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    var fm: FragmentManager? = null
    var fragment: Fragment? = null
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fm = supportFragmentManager
        fragment = fm?.findFragmentById(R.id.fragment_container)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bot)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    fragment = MovieListFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment!!,"TAG1")
                        ?.addToBackStack("firstFrag")
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_home).isChecked = true
                    //Toast.makeText(this,supportFragmentManager.backStackEntryCount,Toast.LENGTH_SHORT).show()
                }
                R.id.nav_fav -> {
                    fragment = FavouritesFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment!!,"TAG2")
                        ?.addToBackStack("Second")
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_fav).isChecked = true
                    Log.d("lol",fm?.backStackEntryCount.toString())

                }
                R.id.nav_profile -> {
                    fragment = UserFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment!!)
                        ?.addToBackStack("Third")
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_profile).isChecked = true

                }
            }
            false
        }
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }
    override fun onBackPressed() {
        if (fm?.backStackEntryCount!! <=1) {
            super.onBackPressed()
        }else
            fm?.popBackStack()
    }

}
