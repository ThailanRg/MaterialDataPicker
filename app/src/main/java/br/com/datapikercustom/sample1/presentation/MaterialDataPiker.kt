package br.com.datapikercustom.sample1.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.datapikercustom.R
import br.com.datapikercustom.databinding.MaterialDataPickerBinding
import br.com.datapikercustom.sample1.presentation.adapter.RepetionAdapter
import br.com.datapikercustom.sample1.presentation.model.DataShedule
import br.com.datapikercustom.sample1.presentation.validator.OnlyThursdaysValidator
import br.com.datapikercustom.sample1.presentation.viewmodel.MaterialDataPikerViewModel
import br.com.datapikercustom.sample1.presentation.viewmodel.MaterialEvent
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale

class MaterialDataPiker : AppCompatActivity() {

    private lateinit var binding: MaterialDataPickerBinding
    private val adapter by lazy { RepetionAdapter(::selectedRepetitionItem) }
    private val viewModel by viewModels<MaterialDataPikerViewModel>()
    private val daySelected = "31/01/2025"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MaterialDataPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapterRepetitionList()
        setupObserver()
        parseToDate()
        setupClick()
    }

    private fun setupObserver() {
        viewModel.state.observeForever { date ->
            binding.tvSelectedDate.text = date
        }
    }

    private fun setupAdapterRepetitionList() = with(binding) {
        adapter.setHasStableIds(true)
        recycler.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupClick() = with(binding) {
        repetitionBlock.setOnClickListener {
            recycler.isVisible = !recycler.isVisible
        }

        timeEndBlock.setOnClickListener {
            buildMaterialDataPicker()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildMaterialDataPicker() {

        val constraints = CalendarConstraints.Builder()
            .setStart(getFirstThursday().timeInMillis)
            .setEnd(getLastThursday().timeInMillis)
            .setValidator(OnlyThursdaysValidator())
            .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione uma Quinta-feira")
                .setTheme(R.style.MyCustomDatePicker4)
                .setCalendarConstraints(constraints)
                .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            displaySelectedDate(selection)
        }

        datePicker.show(supportFragmentManager, TAG_MATERIAL_DATA_PICKER)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displaySelectedDate(
        selection: Long,
    ) {
        val data = Instant
            .ofEpochMilli(selection)
            .atZone(ZoneId.of("America/Sao_Paulo"))
            .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
            .toLocalDate()

        val defineMask = DateTimeFormatter.ofPattern(MASK_DATE_END, Locale("pt-br"))
        val putMask = defineMask.format(data)

        viewModel.onEvent(MaterialEvent.ReceiverDateSelected(putMask))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private fun parseToDate(): DataShedule? {
        val inputFormat = DateTimeFormatter.ofPattern(MASK_DATE_END)
        return try {
            val format = LocalDate.parse(daySelected, inputFormat)
            DataShedule(
                day = format.dayOfMonth,
                month = format.month.value - 1,
                year = format.year
            )
        } catch (e: DateTimeParseException) {
            Log.e("DataParser", "Erro ao converter data: ${e.message}")
            null
        }
    }

    private fun selectedRepetitionItem(option: String) {
        adapter.updateSelectedPerson(option)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFirstThursday(): Calendar {

        val calendar = Calendar.getInstance()

        calendar.set(
            parseToDate()?.year.orZero(),
            parseToDate()?.month.orZero(),
            parseToDate()?.day.orZero()
        )

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return calendar
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLastThursday(): Calendar {

        val dateSelected = LocalDate.of(
            parseToDate()?.year.orZero(),
            parseToDate()?.month.orZero() + 1,
            parseToDate()?.day.orZero()
        )

        val dateEnd = dateSelected.plusDays(ADD_365)
        val calendar = Calendar.getInstance()

        calendar.set(
            dateEnd.year,
            dateEnd.month.value - 1,
            dateEnd.dayOfMonth
        )

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        return calendar
    }

    private fun Int?.orZero() = this ?: 0

    companion object {
        const val TAG_MATERIAL_DATA_PICKER = "DATE_PICKER"
        const val MASK_DATE_END = "dd/MM/yyyy"
        const val ADD_365 = 365L
    }

}

