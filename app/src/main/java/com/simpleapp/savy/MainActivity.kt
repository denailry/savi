package com.simpleapp.savy

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.extensions.LayoutContainer
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.simpleapp.savy.calculator.CalculatorActivity
import com.simpleapp.savy.history.HistoryActivity
import com.simpleapp.savy.lib.PublicMethods
import com.simpleapp.savy.model.record.DailyRecord
import com.simpleapp.savy.model.Date
import com.simpleapp.savy.model.Wallet
import com.simpleapp.savy.model.record.Record
import com.simpleapp.savy.model.suggestion.Aggregator

class MainActivity : AppCompatActivity() {

    companion object {
        val RESULT_CALCULATOR = 1
        val RESULT_HISTORY = 2
        val TYPE_INCOME: Byte = 0
        val TYPE_EXPENSE: Byte = 1
        val TYPE_BALANCE: Byte = 3
    }

    private var activityType = Record.EXPENSE
    private var records: DailyRecord? = null
    private var nameTextWatcher: NameTextWatcher? = null
    private var currentDate = Date.Today()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameTextWatcher = NameTextWatcher(this)
        setClickListener(this)
        setupAutoComplete()
    }

    override fun onResume() {
        super.onResume()
        configureDateChange(currentDate)
        nameTextWatcher?.resetAdapter(inActivityName.text.toString())
        refreshBalance()
    }

    private fun refreshBalance() {
        val wallet = Wallet.getWallet("My Wallet")
        setValue(wallet.balance.toString(), TYPE_BALANCE)
    }

    private fun setupAutoComplete() {
        inActivityName.threshold = 1
        inActivityName.addTextChangedListener(nameTextWatcher)
        inActivityName.setOnItemClickListener { parent, view, position, id ->
            inActivityValue.setText(PublicMethods.moneyFormat(Aggregator.valueOf(inActivityName.text.toString()).toString()))
        }
    }

    inner class NameTextWatcher(val ctx: Context) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            inActivityName.removeTextChangedListener(this)
            s?.replace(0, s.length, normalize(s.toString()))
            inActivityName.addTextChangedListener(this)
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (count == 1 && start == 0) {
                resetAdapter(s)
                btnSaveActivity.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            } else if (count == 0 && start == 0) {
                btnSaveActivity.setBackgroundColor(resources.getColor(R.color.disabled))
            }
        }
        fun resetAdapter(s : CharSequence?) {
            val ag = Aggregator.aggregate(s.toString())
            if (ag != null) {
                val suggestion = ag.map { it.name }
                val adapter = ArrayAdapter<String>(ctx,
                        R.layout.select_dialog_item_material, suggestion)
                inActivityName.setAdapter(adapter)
            }
        }
    }

    private fun normalize(s : String) : String {
        var res = ""
        var prevC : Char? = null
        for (i : Int in 0.until(s.length)) {
            if (prevC == null || prevC == ' ') {
                if (s[i] in 'a'..'z') {
                    res += s[i].toUpperCase()
                } else if (s[i] != ' ') {
                    res += s[i]
                }
            } else {
                res += s[i]
            }
            prevC = s[i]
        }
        return res
    }

    private fun configureDateChange(date: Date) {
        this.records = Wallet.getWallet("My Wallet").getRecords(date.toDatestamp())
        setValue(this.records!!.income.toString(), TYPE_INCOME)
        setValue(this.records!!.expense.toString(), TYPE_EXPENSE)
        tvDayNumber.text = date.day.toString()
        tvMonthMain.text = Date.MonthName(date.month)?.substring(0, 3)
        tvYearMain.text = date.year.toString()
        tvDayNameMain.text = date.getDayName()?.substring(0, 3)
    }

    private fun setValue(value: String, type: Byte) {
        when(type) {
            TYPE_INCOME -> {
                if (value.length >= 9) {
                    tvIncome.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.in_ex_sum_small))
                } else {
                    tvIncome.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.in_ex_sum_normal))
                }
                tvIncome.text = PublicMethods.moneyFormat(value)
            }
            TYPE_EXPENSE -> {
                if (value.length >= 9) {
                    tvExpense.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.in_ex_sum_small))
                } else {
                    tvExpense.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.in_ex_sum_normal))
                }
                tvExpense.text = PublicMethods.moneyFormat(value)
            }
            TYPE_BALANCE -> {
                if (value.length >= 9) {
                    tvBalance.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.balance_sum_small))
                } else {
                    tvBalance.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.balance_sum_normal))
                }
                tvBalance.text = PublicMethods.moneyFormat(value)
            }
        }
    }

    private fun setClickListener(context : Context) {
        btnSaveActivity.setOnClickListener(OnClickSave(this))
        viewActivityType.setOnClickListener { _ ->
            if (activityType == Record.EXPENSE) {
                activityType = Record.INCOME
                tvTypeIncome.setBackgroundColor(resources.getColor(R.color.colorAccent))
                tvTypeExpense.setBackgroundColor(resources.getColor(R.color.unselected))
            } else {
                activityType = Record.EXPENSE
                tvTypeIncome.setBackgroundColor(resources.getColor(R.color.unselected))
                tvTypeExpense.setBackgroundColor(resources.getColor(R.color.colorAccent))
            }
        }
        inActivityValue.setOnClickListener{_ ->
            val value : Long
            if (inActivityValue.text.length == 0) {
                value = 0
            } else {
                value = inActivityValue.text.toString().replace(".","").toLong()
            }
            intent = Intent(this, CalculatorActivity::class.java)
            intent.putExtra("value", value)
            startActivityForResult(intent, RESULT_CALCULATOR)
        }
        tvHistory.setOnClickListener{_ ->
            val intent = Intent(context, HistoryActivity::class.java)
            intent.putExtra("datestamp", this.records!!.date.toDatestamp())
            startActivityForResult(intent, RESULT_HISTORY)
        }
        viewDate.setOnClickListener{_ ->
            val fragment = DatePickerFragment.Builder(this.records!!.date, OnDateSetListener(viewRootMain))
            fragment.show(supportFragmentManager, "date")
        }
        btnPreviousDay.setOnClickListener{ _ ->
            changeDate(records!!.date.yesterday())
        }
        btnNextDay.setOnClickListener{ _ ->
            changeDate(records!!.date.tommorow())
        }
    }

    private fun changeDate(newDate: Date) {
        currentDate = newDate
        configureDateChange(currentDate)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                RESULT_CALCULATOR -> inActivityValue.setText(
                        PublicMethods.moneyFormat(data!!.getLongExtra("value", 0).toString()))
            }
        }
        when(requestCode) {
            RESULT_HISTORY -> changeDate(
                    Date(data!!.getIntExtra("datestamp", Date.Today().toDatestamp())))
        }
    }

    inner class OnDateSetListener(override val containerView: View?) : DatePickerDialog.OnDateSetListener, LayoutContainer {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            changeDate(Date(dayOfMonth, month, year))
        }
    }

    inner class OnClickSave(val context: Context) : View.OnClickListener {
        fun resetInput() {
            inActivityName.setText("")
            inActivityValue.setText("0")
            inActivityNote.setText("")
        }

        fun saveNewActivity() {
            val name = inActivityName.text.toString()
            val value = inActivityValue.text.toString().replace(".","").toLong()
            val notes = inActivityNote.text.toString()
            records!!.saveNewActivity(name, value, notes, activityType)
            resetInput()
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        }

        fun updateActivitySummary() {
            when(activityType) {
                Record.INCOME -> setValue(records!!.income.toString(), TYPE_INCOME)
                Record.EXPENSE -> setValue(records!!.expense.toString(), TYPE_EXPENSE)            }
            refreshBalance()
        }
        override fun onClick(v: View?) {
            if (inActivityName.text.length > 0) {
                saveNewActivity()
                updateActivitySummary()
            }
        }
    }
}
