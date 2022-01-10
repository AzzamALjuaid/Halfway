package com.tuwaiq.halfway.screens

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

class TabsPager(val fragmentManager: FragmentManager, val list: List<Fragment>) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount()= list.size

    override fun getItem(position: Int): Fragment {
       return list.get(position)
    }

}
