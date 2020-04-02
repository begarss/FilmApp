package com.example.themovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.themovie.adapter.SlidePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var pager: ViewPager;
    lateinit var pagerAdapter: PagerAdapter
    val f1: Fragment = MoviesListFragment();
    val list: MutableList<Fragment> = ArrayList<Fragment>()
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bot)
        pager = findViewById(R.id.pager)
        list.add(f1)

        pagerAdapter = SlidePagerAdapter(
            supportFragmentManager,
            list
        )
        pager.adapter = pagerAdapter
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    pager.setCurrentItem(0, false)
                    bottomNavigationView.menu.findItem(R.id.nav_home).isChecked = true

                    bottomNavigationView.menu.findItem(R.id.nav_home)
                        .setIcon(R.drawable.ic_home_black_24dp)
                }
                R.id.nav_book -> {
                    pager.setCurrentItem(1, false)
                    bottomNavigationView.menu.findItem(R.id.nav_book).isChecked = true
                }
            }//bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(false);
            false
        }


    }


}
