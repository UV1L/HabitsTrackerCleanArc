package com.example.habitstracker.ui.main

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.Habit
import com.example.habitstracker.R
import com.example.habitstracker.application.MyApplication
import com.example.habitstracker.fabric.MainViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddHabitFragment : Fragment() {

    companion object {
        const val REQUIRED_MESSAGE = "required*"
    }

    private lateinit var viewModel: MainViewModel
    private var selectedColor: Int? = null
    private var name: String? = null
    private var about: String? = null
    private var type: String? = null
    private var priority: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_habit, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainUseCase = (requireActivity().application as MyApplication)
            .applicationComponent
            .inject()

        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(mainUseCase)).get(
            MainViewModel::class.java
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners() {
        setTextInputListeners()
        setColorPicker()
        setCalendar()
        setOkBtnListener()
    }

    private fun setTextInputListeners() {
        view?.findViewById<TextInputEditText>(R.id.fragmentAddAbout)
            ?.doOnTextChanged { text, start, count, after ->
                val aboutContainerView =
                    view?.findViewById<TextInputLayout>(R.id.fragmentAddAboutContainer)
                about = text.toString()
                textInputOnTextChanged(text, aboutContainerView!!)
            }
        view?.findViewById<TextInputEditText>(R.id.fragmentAddName)
            ?.doOnTextChanged { text, start, count, after ->
                val nameContainerView =
                    view?.findViewById<TextInputLayout>(R.id.fragmentAddNameContainer)
                name = text.toString()
                textInputOnTextChanged(text, nameContainerView!!)
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
        val circle = requireView().findViewById<ImageView>(R.id.fragmentAddColorCircle)
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
        val background = circle.background
        val gradientDrawable = background as GradientDrawable
        gradientDrawable.color = ColorStateList.valueOf(selectedColor!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCalendar() {
        val calendarImgView =
            requireView().findViewById<ImageView>(R.id.fragmentAddCalendarImg)
        calendarImgView.setOnClickListener {
            MaterialDatePicker.Builder
                .dateRangePicker().build()
                .apply {
                    addOnPositiveButtonClickListener { dateInMillis ->
                        onDateSelected(dateInMillis)
                    }
                }.show(parentFragmentManager, MaterialDatePicker::class.java.canonicalName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onDateSelected(dateTimeStampInMillis: Pair<Long, Long>) {
        var dateTime: LocalDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dateTimeStampInMillis.first),
            ZoneId.systemDefault()
        )
        var dateAsFormattedText: String = dateTime.format(DateTimeFormatter.ofPattern("MM-dd"))
        requireView().findViewById<TextView>(R.id.fragmentAddFirstDate).text =
            dateAsFormattedText

        dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dateTimeStampInMillis.second),
            ZoneId.systemDefault()
        )
        dateAsFormattedText = dateTime.format(DateTimeFormatter.ofPattern("MM-dd"))
        requireView().findViewById<TextView>(R.id.fragmentAddSecondDate).text =
            dateAsFormattedText

        requireView().findViewById<View>(R.id.fragmentAddDateSeparator).visibility =
            View.VISIBLE
    }

    private fun setOkBtnListener() {
        val btn = view?.findViewById<Button>(R.id.fragmentAddOkBtn)

        btn?.setOnClickListener {
            setHabitType()
            setHabitPriority()

            if (name != null &&
                about != null &&
                type != null &&
                priority != null &&
                selectedColor != null
            ) {
                val newHabit = Habit(name!!, about!!, type!!, priority!!, selectedColor!!)

                viewModel.viewModelScope.launch {
                    viewModel.addHabit(newHabit)
                }
            }
        }
    }

    private fun setHabitType() {
        val radioGroupView =
            requireView().findViewById<RadioGroup>(R.id.fragmentAddPriorityRadioGroup)

        val selectedRadioBtn =
            requireView().findViewById<RadioButton>(radioGroupView.checkedRadioButtonId)

        type = selectedRadioBtn.text.toString()
    }

    private fun setHabitPriority() {
        val priorityView = requireView().findViewById<Spinner>(R.id.fragmentAddPriority)
        priority = priorityView.selectedItem.toString()
    }
}