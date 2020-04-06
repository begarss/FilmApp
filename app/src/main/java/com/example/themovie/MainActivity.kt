package com.example.themovie

import android.os.Bundle
import android.view.Window
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val window: Window = this.getWindow()

        fm = supportFragmentManager
        fragment = fm?.findFragmentById(R.id.fragment_container)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bot)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    fragment = MovieListFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment!!)
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_home).isChecked = true

                }
                R.id.nav_fav ->{
                    fragment = FavouritesFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container,fragment!!)
                        ?.commit()
                    bottomNavigationView.menu.findItem(R.id.nav_fav).isChecked = true

                }
                R.id.nav_profile -> {
                    fragment = UserFragment()
                    fm?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment!!)
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

}
