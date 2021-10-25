package com.example.habitstracker.ui.main.main_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.habitstracker.R
import com.example.habitstracker.adapters.MainPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.example.habitstracker.databinding.MainFragmentBinding
import com.example.habitstracker.ui.main.AddHabitFragment
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialElevationScale

class MainPageContainerFragment : Fragment() {

    companion object {
        fun newInstance() = MainPageContainerFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setFabListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(false)
    }

    private fun setupTabLayout() {

        val viewPager = binding.mainFragmentViewPager
        viewPager.adapter = MainPagerAdapter(childFragmentManager)
        viewPager.addOnPageChangeListener(MainPageChangeListener(this))
        val tabLayout = binding.mainFragmentTabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setFabListener() {

        binding.mainFragmentFab.setOnClickListener {

            val addHabitFragment = AddHabitFragment()
            addHabitFragment.sharedElementEnterTransition = MaterialArcMotion()

            parentFragmentManager.beginTransaction()
                .addSharedElement(requireView(), "shared_element_container")
                .replace(R.id.container, AddHabitFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    class MainPageChangeListener(private val container: MainPageContainerFragment) :
        ViewPager.SimpleOnPageChangeListener() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            container.binding.mainFragmentFab.show()
        }
    }
}