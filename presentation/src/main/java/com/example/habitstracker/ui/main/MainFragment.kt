package com.example.habitstracker.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.domain.entities.Habit
import com.example.habitstracker.R
import com.example.habitstracker.adapters.MainPagerAdapter
import com.example.habitstracker.adapters.RecyclerViewAdapter
import com.example.habitstracker.application.MyApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.example.extensions.observe
import com.example.habitstracker.fabric.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setFabListener()
    }

    private fun setupTabLayout() {
        val viewPager = requireView().findViewById<ViewPager>(R.id.mainFragmentViewPager)
        viewPager?.adapter = MainPagerAdapter(childFragmentManager)
        val tabLayout = requireView().findViewById<TabLayout>(R.id.mainFragmentTabLayout)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setFabListener() {
        requireView().findViewById<FloatingActionButton>(R.id.mainFragmentFab).setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddHabitFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}