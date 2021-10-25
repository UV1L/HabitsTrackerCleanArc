package com.example.habitstracker.ui.main.recycler_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.extensions.*
import com.example.habitstracker.R
import com.example.habitstracker.adapters.RecyclerViewAdapter
import com.example.habitstracker.application.MyApplication
import com.example.habitstracker.databinding.ListHabitsBinding
import com.example.habitstracker.fabric.MainViewModelFactory
import com.example.habitstracker.ui.main.AddHabitFragment
import com.example.habitstracker.ui.main.HabitViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecyclerViewFragment(
    private val habitType: Int
) : Fragment(),
    RecyclerViewAdapter.HabitOnClickListener {

    companion object {

        private const val MIN_FAB_OFFSET = 0
    }

    private lateinit var viewModel: HabitViewModel
    private lateinit var adapter: RecyclerViewAdapter

    private var _binding: ListHabitsBinding? = null
    private val binding get() = _binding!!
    private val fab: FloatingActionButton
        get() = requireParentFragment().requireView().findViewById(R.id.mainFragmentFab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainUseCase = (requireActivity().application as MyApplication)
            .applicationComponent
            .inject()

        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(mainUseCase))
            .get(HabitViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ListHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val recyclerView = binding.listHabitsRecView
        recyclerView.let { setOnScrollListener(it) }
        adapter = RecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.habits.filter { it.type == habitType }
            .observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private fun setOnScrollListener(recView: RecyclerView) {

        recView.addOnScrollListener(MyOnScrollListener(fab))
    }

    class MyOnScrollListener(private val fab: FloatingActionButton) :
        RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            if (dy < MIN_FAB_OFFSET && !fab.isShown)
                fab.show()
            else if (dy > MIN_FAB_OFFSET && fab.isShown)
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
