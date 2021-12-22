package com.example.habitstracker.ui.main

import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.doOnAttach
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entities.Habit
import com.example.habitstracker.R
import com.example.habitstracker.application.MyApplication
import com.example.habitstracker.databinding.FragmentAddHabitBinding
import com.example.habitstracker.fabric.MainViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import dev.sasikanth.colorsheet.ColorSheet
import java.lang.ref.WeakReference
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable

import androidx.appcompat.content.res.AppCompatResources




class AddHabitFragment(private val habit: Habit? = null) : Fragment() {

    companion object {
        const val REQUIRED_MESSAGE = "required*"
    }

    private lateinit var viewModel: HabitViewModel
    private var selectedColor: Int? = habit?.color
    private var title: String? = habit?.title
    private var description: String? = habit?.description
    private var type: Int? = habit?.type
    private var priority: Int? = habit?.priority
    private var frequency: Int? = habit?.frequency
    private var count: Int? = habit?.count

    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainUseCase = (requireActivity().application as MyApplication)
            .applicationComponent
            .inject()

        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(mainUseCase))
            .get(HabitViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habit?.apply {
            onRestoreInstanceState()
        }

        setListeners()
    }

    @Suppress("UNCHECKED_CAST")
    private fun onRestoreInstanceState() {

        binding.fragmentAddDescription.text =
            SpannableStringBuilder(description)
        binding.fragmentAddTitle.text =
            SpannableStringBuilder(title)

        binding.fragmentAddPeriodEditTxt.text =
            SpannableStringBuilder(frequency.toString())

        binding.fragmentAddCountEditTxt.text =
            SpannableStringBuilder(count.toString())

        setColor(binding.fragmentAddColorCircle)

        val typeRadioGroup = binding.fragmentAddTypeRadioGroup
        when (type) {
            0 -> typeRadioGroup.check(R.id.fragmentAddGoodRadioBtn)
            else -> typeRadioGroup.check(R.id.fragmentAddBadRadioBtn)
        }

        val prioritySpinner = binding.fragmentAddPriority
        val arrayAdapter = prioritySpinner.adapter as ArrayAdapter<String>
        val selection = when (priority) {
            0 -> arrayAdapter.getPosition("Низкий")
            1 -> arrayAdapter.getPosition("Средний")
            else -> arrayAdapter.getPosition("Большой")
        }
        prioritySpinner.setSelection(selection)
    }

    private fun setListeners() {

        setTextInputListeners()
        setColorPicker()
        setOkBtnListener()
        setCancelBtnListener()
    }

    private fun setTextInputListeners() {

        binding.fragmentAddTitle.doOnTextChanged { text, _, _, _ ->

            title = text.toString()
            val titleContainer = binding.fragmentAddTitleContainer
            textInputOnTextChanged(text, titleContainer)
        }

        binding.fragmentAddTitle.doOnAttach {

            val titleContainer = binding.fragmentAddTitleContainer
            textInputOnTextChanged(binding.fragmentAddTitle.text, titleContainer)
        }

        binding.fragmentAddDescription.doOnTextChanged { text, _, _, _ ->

            description = text.toString()
            val descriptionContainer = binding.fragmentAddDescriptionContainer
            textInputOnTextChanged(text, descriptionContainer)
        }

        binding.fragmentAddDescription.doOnAttach {

            val descriptionContainer = binding.fragmentAddDescriptionContainer
            textInputOnTextChanged(binding.fragmentAddTitle.text, descriptionContainer)
        }

        binding.fragmentAddPeriodEditTxt.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty())
                frequency = text.toString().toInt()
        }

        binding.fragmentAddCountEditTxt.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty())
                count = text.toString().toInt()
        }
    }

    private fun textInputOnTextChanged(text: CharSequence?, container: TextInputLayout) {

        text?.let {

            if (it.isNotEmpty()) {
                container.helperText = null
            } else {
                container.helperText = REQUIRED_MESSAGE
            }
        }
    }

    private fun setColorPicker() {

        val colors = resources.getIntArray(R.array.colors)
        val circle = binding.fragmentAddColorCircle

        circle.setOnClickListener {

            ColorSheet().colorPicker(
                colors = colors,
                noColorOption = false,
                listener = { color ->
                    selectedColor = color
                    setColor(circle)
                })
                .show(parentFragmentManager)
        }
    }

    private fun setColor(circle: ImageView) {

        circle.backgroundTintList = ColorStateList.valueOf(selectedColor!!)
    }

    private fun setOkBtnListener() {

        val okBtn = binding.fragmentAddOkBtn

        okBtn.setOnClickListener(
            ButtonListener(WeakReference(parentFragmentManager)) { setHabit() }
        )
    }

    private fun setCancelBtnListener() {

        val cancelBtn = binding.fragmentAddCancelBtn

        cancelBtn.setOnClickListener(
            ButtonListener(WeakReference(parentFragmentManager))
        )
    }

    private fun setHabit(): Boolean {

        setHabitType()
        setHabitPriority()

        if (title != null &&
            description != null &&
            type != null &&
            priority != null &&
            selectedColor != null &&
            frequency != null &&
            count != null
        ) {
            val newHabit =
                Habit(
                    title!!,
                    description!!,
                    type!!,
                    priority!!,
                    selectedColor!!,
                    frequency!!,
                    count!!
                )

            addHabit(newHabit)
            return true
        } else {
            Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun addHabit(newHabit: Habit) {

        if (habit != null) {
            viewModel.updateHabit(oldHabit = habit, newHabit = newHabit)
        } else {
            viewModel.load(newHabit)
        }
    }

    private fun setHabitType() {
        val radioGroupView =
            binding.fragmentAddTypeRadioGroup

        val selectedRadioBtn =
            requireView().findViewById<RadioButton>(radioGroupView.checkedRadioButtonId)

        val stringType = selectedRadioBtn?.text.toString()
        type = when (stringType) {
            "Хороший" -> 0
            "Плохой" -> 1
            else -> null
        }
    }

    private fun setHabitPriority() {
        val priorityView = binding.fragmentAddPriority
        val stringPriority = priorityView.selectedItem.toString()

        priority = when (stringPriority) {
            "Низкий" -> 0
            "Средний" -> 1
            "Большой" -> 2
            else -> null
        }
    }
}

class ButtonListener(
    private val fm: WeakReference<FragmentManager>,
    private val f: (() -> Boolean)? = null
) :
    View.OnClickListener {

    override fun onClick(v: View?) {

        f?.let {
            if (!it())
                return
        }

        fm.get()?.popBackStack()
    }
}