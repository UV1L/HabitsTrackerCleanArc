package com.example.habitstracker.ui.main

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
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.Habit
import com.example.habitstracker.R
import com.example.habitstracker.application.MyApplication
import com.example.habitstracker.fabric.MainViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddHabitFragment(private val habit: Habit? = null) : Fragment() {

    companion object {
        const val REQUIRED_MESSAGE = "required*"
    }

    private lateinit var viewModel: MainViewModel
    private var selectedColor: Int? = habit?.color
    private var title: String? = habit?.title
    private var description: String? = habit?.description
    private var type: Int? = habit?.type
    private var priority: Int? = habit?.priority
    private var frequency: Int? = habit?.frequency
    private var count: Int? = habit?.count

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_habit, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainUseCase = (requireActivity().application as MyApplication)
            .applicationComponent
            .inject()

        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(mainUseCase))
            .get(MainViewModel::class.java)
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
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun onRestoreInstanceState() {
        requireView().findViewById<TextInputEditText>(R.id.fragmentAddDescription).text =
            SpannableStringBuilder(description)

        requireView().findViewById<TextInputEditText>(R.id.fragmentAddTitle).text =
            SpannableStringBuilder(title)

        requireView().findViewById<EditText>(R.id.fragmentAddPeriodEditTxt).text =
            SpannableStringBuilder(frequency.toString())

        requireView().findViewById<EditText>(R.id.fragmentAddCountEditTxt).text =
            SpannableStringBuilder(count.toString())

        requireView().findViewById<ImageView>(R.id.fragmentAddColorCircle).background.colorFilter =
            BlendModeColorFilter(selectedColor!!, BlendMode.SRC_ATOP)

        val typeRadioGroup = requireView().findViewById<RadioGroup>(R.id.fragmentAddTypeRadioGroup)
        when (type) {
            0 -> typeRadioGroup.check(R.id.fragmentAddGoodRadioBtn)
            else -> typeRadioGroup.check(R.id.fragmentAddBadRadioBtn)
        }

        val prioritySpinner = requireView().findViewById<Spinner>(R.id.fragmentAddPriority)
        val arrayAdapter = prioritySpinner.adapter as ArrayAdapter<String>
        val selection = when (priority) {
            0 -> arrayAdapter.getPosition("Низкий")
            1 -> arrayAdapter.getPosition("Средний")
            else -> arrayAdapter.getPosition("Большой")
        }
        prioritySpinner.setSelection(selection)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setListeners() {
        setTextInputListeners()
        setColorPicker()
        setOkBtnListener()
    }

    private fun setTextInputListeners() {
        view?.findViewById<TextInputEditText>(R.id.fragmentAddDescription)
            ?.doOnTextChanged { text, _, _, _ ->
                val aboutContainerView =
                    view?.findViewById<TextInputLayout>(R.id.fragmentAddDescriptionContainer)
                description = text.toString()
                textInputOnTextChanged(text, aboutContainerView!!)
            }
        view?.findViewById<TextInputEditText>(R.id.fragmentAddTitle)
            ?.doOnTextChanged { text, _, _, _ ->
                val nameContainerView =
                    view?.findViewById<TextInputLayout>(R.id.fragmentAddTitleContainer)
                title = text.toString()
                textInputOnTextChanged(text, nameContainerView!!)
            }
        view?.findViewById<EditText>(R.id.fragmentAddPeriodEditTxt)
            ?.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty())
                    frequency = text.toString().toInt()
            }
        view?.findViewById<EditText>(R.id.fragmentAddCountEditTxt)
            ?.doOnTextChanged { text, _, _, _ ->
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

    @RequiresApi(Build.VERSION_CODES.Q)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setColor(circle: ImageView) {
        circle.background.colorFilter = BlendModeColorFilter(selectedColor!!, BlendMode.SRC_ATOP)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun setCalendar() {
//        val calendarImgView =
//            requireView().findViewById<ImageView>(R.id.fragmentAddCalendarImg)
//        calendarImgView.setOnClickListener {
//            MaterialDatePicker.Builder
//                .dateRangePicker().build()
//                .apply {
//                    addOnPositiveButtonClickListener { dateInMillis ->
//                        onDateSelected(dateInMillis)
//                    }
//                }.show(parentFragmentManager, MaterialDatePicker::class.java.canonicalName)
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun onDateSelected(dateTimeStampInMillis: Pair<Long, Long>) {
//        var dateTime: LocalDateTime = LocalDateTime.ofInstant(
//            Instant.ofEpochMilli(dateTimeStampInMillis.first),
//            ZoneId.systemDefault()
//        )
//        var dateAsFormattedText: String = dateTime.format(DateTimeFormatter.ofPattern("MM-dd"))
//        requireView().findViewById<TextView>(R.id.fragmentAddFirstDate).text =
//            dateAsFormattedText
//
//        date = date?.plus(dateAsFormattedText) ?: dateAsFormattedText
//
//        dateTime = LocalDateTime.ofInstant(
//            Instant.ofEpochMilli(dateTimeStampInMillis.second),
//            ZoneId.systemDefault()
//        )
//        dateAsFormattedText = dateTime.format(DateTimeFormatter.ofPattern("MM-dd"))
//        requireView().findViewById<TextView>(R.id.fragmentAddSecondDate).text =
//            dateAsFormattedText
//
//        date = date!!.plus(" - $dateAsFormattedText")
//
//        requireView().findViewById<View>(R.id.fragmentAddDateSeparator).visibility =
//            View.VISIBLE
//    }

    private fun setOkBtnListener() {
        val btn = view?.findViewById<Button>(R.id.fragmentAddOkBtn)

        btn?.setOnClickListener {
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
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addHabit(newHabit: Habit) {

        if (habit != null) {
            viewModel.updateHabit(oldHabit = habit, newHabit = newHabit)
        }
        else {
            viewModel.load(newHabit)
        }
    }

    private fun setHabitType() {
        val radioGroupView =
            requireView().findViewById<RadioGroup>(R.id.fragmentAddTypeRadioGroup)

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
        val priorityView = requireView().findViewById<Spinner>(R.id.fragmentAddPriority)
        val stringPriority = priorityView.selectedItem.toString()

        priority = when (stringPriority) {
            "Низкий" -> 0
            "Средний" -> 1
            "Большой" -> 2
            else -> null
        }
    }
}