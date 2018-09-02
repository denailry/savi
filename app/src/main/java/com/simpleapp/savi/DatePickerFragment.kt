package com.simpleapp.savi

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.simpleapp.savi.model.Date

class DatePickerFragment : DialogFragment() {

    companion object {
        fun Builder(date: Date, onDateSetListener: DatePickerDialog.OnDateSetListener) : DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.onDateSetListener = onDateSetListener
            fragment.date = date
            return fragment
        }
    }

    var onDateSetListener : DatePickerDialog.OnDateSetListener? = null
    var date : Date? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context,
                onDateSetListener, date!!.year, date!!.month, date!!.day)
    }
}