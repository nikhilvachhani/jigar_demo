package com.frami.ui.common

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.frami.R
import java.util.*

class DatePickerFragment//        this.is18YearsOld = is18YearsOld//        is18YearsOld: Boolean,
    (
    private var isSetMinDate: Boolean = false,
    private var isSetPreSelectedMaxDate: Boolean = false,
    private var isSetCurrentDateMaxDate: Boolean = false,
    private var isMin18YearsOld: Boolean = false,
    private var dateSelectListener: DateSelectListener? = null,
    private var forWhom: String = "",
    private var minDateCal: Calendar = Calendar.getInstance(),
    private var preSelectedCal: Calendar = Calendar.getInstance(),
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = preSelectedCal
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val dialog = DatePickerDialog(requireContext(), R.style.DialogTheme, this, year, month, day)

        if (isSetMinDate) {
            dialog.datePicker.minDate = minDateCal.timeInMillis
        }
        if (isMin18YearsOld) {
            val min18YearOldCal = Calendar.getInstance()
            min18YearOldCal.add(Calendar.YEAR, - 18);
            dialog.datePicker.maxDate = min18YearOldCal.timeInMillis;
        }

        if (isSetCurrentDateMaxDate) {
            dialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        }
        if (isSetPreSelectedMaxDate) {
            dialog.datePicker.maxDate = c.timeInMillis
        }

        return dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        dateSelectListener?.onDateSet(year, month + 1, day, forWhom)
    }

    interface DateSelectListener {
        fun onDateSet(year: Int, month: Int, day: Int, forWhom: String)
    }
}