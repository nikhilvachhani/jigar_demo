package com.frami.ui.common

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.frami.R
import com.frami.utils.AppConstants
import java.util.*

class TimePickerFragment
    (
    private var dateSelectListener: DateSelectListener,
    private var forWhom: String = "",
    private var preSelectedCal: Calendar = Calendar.getInstance(),
    private var ampm: String = ""
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour =
            if (ampm.isBlank() || ampm === AppConstants.KEYS.AM) preSelectedCal[Calendar.HOUR_OF_DAY] else preSelectedCal[Calendar.HOUR_OF_DAY] + 12
        val minute = preSelectedCal[Calendar.MINUTE]

        val dialog = TimePickerDialog(
            requireContext(),
            R.style.DialogTheme,
            this,
            hour,
            minute,
            if (ampm.isBlank()) true else DateFormat.is24HourFormat(requireActivity())
        )

        return dialog
    }

    override fun onTimeSet(view: TimePicker, hours: Int, minute: Int) {
        val AM_PM: String = if (hours < 12) AppConstants.KEYS.AM else AppConstants.KEYS.PM
        val newHours = if (ampm.isBlank()) hours else if (hours < 12) hours else hours - 12
        dateSelectListener.onTimeSet(newHours, minute, AM_PM, forWhom)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val hours = preSelectedCal[Calendar.HOUR_OF_DAY]
        val minute = preSelectedCal[Calendar.MINUTE]
        dateSelectListener.onTimeSet(hours, minute, ampm, forWhom)
    }

    interface DateSelectListener {
        fun onTimeSet(hour: Int, minute: Int, ampm: String, forWhom: String)
    }
}