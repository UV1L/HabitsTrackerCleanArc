package com.example.habitstracker.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Habit
import com.example.extensions.*
import com.example.habitstracker.R
import com.example.habitstracker.adapters.RecyclerViewAdapter
import com.example.habitstracker.application.MyApplication
import com.example.habitstracker.fabric.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers

class RecyclerViewFragment(
    private val habitType: Int
) : Fragment(),
    RecyclerViewAdapter.HabitOnClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainUseCase = (requireActivity().application as MyApplication)
            .applicationComponent
            .inject()

        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(mainUseCase)).get(
            MainViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_habits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.listHabitsRecView)
        recyclerView?.let { setOnScrollListener(it) }
        adapter = RecyclerViewAdapter(this)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)

        viewModel.habits.filter { it.type == habitType }
            .observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private fun setOnScrollListener(recView: RecyclerView) {
        parentFragment?.view?.findViewById<FloatingActionButton>(R.id.mainFragmentFab)
            ?.apply { recView.addOnScrollListener(MyOnScrollListener(this)) }
    }

    class MyOnScrollListener(private val fab: FloatingActionButton) :
        RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy < 0 && !fab.isShown)
                fab.show()
            else if (dy > 0 && fab.isShown)
                fab.hide()
        }
    }

    override fun onClick(habitPosition: Int) {
        val habit = viewModel.habits.value
            ?.filter { it.type == habitType }
            ?.let {
                it[habitPosition]
            }

        parentFragment?.parentFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.container, AddHabitFragment(habit))
            ?.addToBackStack(null)
            ?.commit()
    }
}
