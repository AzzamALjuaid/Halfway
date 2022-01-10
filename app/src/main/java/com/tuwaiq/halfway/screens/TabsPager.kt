package com.tuwaiq.halfway.screens

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabsPager(val fragmentManager: FragmentManager, val list: List<Fragment>) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount()= list.size

    override fun getItem(position: Int): Fragment {
        return list.get(position)
    }
}