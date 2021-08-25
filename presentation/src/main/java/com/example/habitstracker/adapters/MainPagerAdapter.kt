package com.example.habitstracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.habitstracker.ui.main.MainFragment
import com.example.habitstracker.ui.main.RecyclerViewFragment

class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return RecyclerViewFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            0 -> "Хорошие"
            else -> "Плохие"
        }
    }
}