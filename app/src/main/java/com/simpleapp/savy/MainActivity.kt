package com.simpleapp.savy

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.extensions.LayoutContainer
import android.content.Context
import android.graphics.Color
import android.widget.DatePicker
import com.simpleapp.savy.calculator.CalculatorActivity
import com.simpleapp.savy.history.HistoryActivity
import com.simpleapp.savy.model.record.DailyRecord
import com.simpleapp.savy.model.Date
import com.simpleapp.savy.model.record.Record

class MainActivity : AppCompatActivity() {

    private var activityType = Record.EXPENSE
    private var records: DailyRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        configureDateChange(Date.Today())
    }

    private fun configureDateChange(date: Date) {
        this.records = DailyRecord(date.toDatestamp())
        tvIncome.text = this.records!!.income.toString()
        tvExpense.text = this.records!!.expense.toString()
        tvDayNumber.text = date.day.toString()
        tvMonthMain.text = Date.MonthName(date.month)
        tvYearMain.text = date.year.toString()
        tvDayNameMain.text = date.getDayName()
    }

    private fun setClickListener(context : Context) {
        btnSaveActivity.setOnClickListener(OnClickSave(viewRootMain))
        viewActivityType.setOnClickListener { _ ->
            if (activityType == Record.EXPENSE) {
                activityType = Record.INCOME
                tvTypeIncome.setBackgroundColor(Color.parseColor("#00ae00"))
                tvTypeExpense.setBackgroundColor(Color.parseColor("#aaaaaa"))
            } else {
                activityType = Record.EXPENSE
                tvTypeIncome.setBackgroundColor(Color.parseColor("#aaaaaa"))
                tvTypeExpense.setBackgroundColor(Color.parseColor("#f09500"))
            }
        }
        inActivityValue.setOnClickListener{_ ->
            val value : Long
            if (inActivityValue.text.length == 0) {
                value = 0
            } else {
                value = inActivityValue.text.toString().toLong()
            }
            intent = Intent(this, CalculatorActivity::class.java)
            intent.putExtra("value", value)
            startActivityForResult(intent, 1)
        }
        tvHistory.setOnClickListener{_ ->
            val intent = Intent(context, HistoryActivity::class.java)
            intent.putExtra("datestamp", this.records!!.date.toDatestamp())
            startActivity(intent)
        }
        viewDate.setOnClickListener{_ ->
            val fragment = DatePickerFragment.Builder(this.records!!.date, OnDateSetListener(viewRootMain))
            fragment.show(supportFragmentManager, "date")
        }
        btnPreviousDay.setOnClickListener{ _ ->
            configureDateChange(records!!.date.yesterday())
        }
        btnNextDay.setOnClickListener{ _ ->
            configureDateChange(records!!.date.tommorow())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inActivityValue.setText(data!!.getIntExtra("value", 0).toString())
    }

    inner class OnDateSetListener(override val containerView: View?) : DatePickerDialog.OnDateSetListener, LayoutContainer {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            configureDateChange(Date(dayOfMonth, month, year))
        }
    }

    inner class OnClickSave(override val containerView: View) : View.OnClickListener, LayoutContainer {
        fun resetInput() {
            inActivityName.setText("")
            inActivityValue.setText("")
            inActivityNote.setText("")
        }

        fun saveNewActivity() {
            val name = inActivityName.text.toString()
            val value = inActivityValue.text.toString().toInt()
            val notes = inActivityNote.text.toString()
            records!!.saveNewActivity(name, value, notes, activityType)
            resetInput()
        }

        fun updateActivitySummary() {
            when(activityType) {
                Record.INCOME -> tvIncome.text = records!!.income.toString()
                Record.EXPENSE -> tvExpense.text = records!!.expense.toString()
            }
        }
        override fun onClick(v: View?) {
            saveNewActivity()
            updateActivitySummary()
        }
    }
}
