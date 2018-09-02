package com.simpleapp.savy.recordedit

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.simpleapp.savy.R
import com.simpleapp.savy.TransactionManager
import com.simpleapp.savy.calculator.CalculatorActivity
import com.simpleapp.savy.lib.PublicMethods
import com.simpleapp.savy.model.record.Record
import com.simpleapp.savy.model.suggestion.Aggregator
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_record_edit.*

class RecordEditActivity : AppCompatActivity() {

    private var activityType = Record.EXPENSE
    private var record: Record? = null
    private var previousName: String? = null
    private var previousValue: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_edit)
        initialize()
        setupClickListener()
        setupTextChangeListener()
    }

    private fun initialize() {
        val realm = Realm.getDefaultInstance()
        record = realm.where(Record::class.java)
                .equalTo("id", intent.getLongExtra("id", 0L))
                .findFirst()
        inActivityName.setText(record!!.name)
        inActivityValue.setText(PublicMethods.moneyFormat(record!!.value.toString()))
        inActivityNote.setText(record!!.notes)
        activityType = record!!.type
        previousName = record!!.name
        previousValue = record!!.value
    }

    private fun setupTextChangeListener() {
        inActivityName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1 && start == 0) {
                    btnSaveActivity.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                } else if (count == 0 && start == 0) {
                    btnSaveActivity.setBackgroundColor(resources.getColor(R.color.disabled))
                }
            }
        })
    }

    private fun setupClickListener() {
        btnSaveActivity.setOnClickListener{_ ->
            if (inActivityName.text.length > 0) {
                TransactionManager.Updater(record!!)
                        .setName(inActivityName.text.toString())
                        .setValue(inActivityValue.text.toString().replace(".","").toLong())
                        .setNotes(inActivityNote.text.toString())
                        .setType(activityType)
                finish()
            }
        }
        btnDeleteActivity.setOnClickListener{_ ->
            TransactionManager.delete(record!!)
            finish()
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
            startActivityForResult(intent, 1)
        }
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inActivityValue.setText(PublicMethods.moneyFormat(data!!.getLongExtra("value", 0).toString()))
    }
}
