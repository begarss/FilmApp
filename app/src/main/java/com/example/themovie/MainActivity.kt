package com.example.themovie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.themovie.fragment.MovieListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var fm: FragmentManager? = null
    var fragment: Fragment? = null

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
                        ?.replace(R.id.fragment_container, fragment!!)
                        ?.commit()
                }
            }
            false
        }
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

}
