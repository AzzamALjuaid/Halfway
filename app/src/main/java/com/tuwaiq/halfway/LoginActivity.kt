package com.tuwaiq.halfway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class LoginActivity : AppCompatActivity() {


    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager:ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tabLayout
        tabLayout = findViewById(R.id.tap_layout)
        viewPager = findViewById(R.id.view_pager)
        var googleBTN = findViewById<FloatingActionButton>(R.id.fab_google)

        tabLayout.addTab(tabLayout.newTab().setText("tab"))
        tabLayout.addTab(tabLayout.newTab().setText("Login"))
        tabLayout.addTab(tabLayout.newTab().setText("Signup"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }
}