package com.simpleapp.savi.recordedit

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.simpleapp.savi.R
import com.simpleapp.savi.TransactionManager
import com.simpleapp.savi.calculator.CalculatorActivity
import com.simpleapp.savi.lib.PublicMethods
import com.simpleapp.savi.model.record.Record
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
        inActivityName.addTextChangedListener(NameTextWatcher())
        inActivityValue.setText(PublicMethods.moneyFormat(record!!.value.toString()))
        inActivityNote.setText(record!!.notes)
        previousName = record!!.name
        previousValue = record!!.value
        activityType = record!!.type
        refreshActivityType()
    }

    inner class NameTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            inActivityName.removeTextChangedListener(this)
            s?.replace(0, s.length, normalize(s.toString()))
            inActivityName.addTextChangedListener(this)
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (count == 1 && start == 0) {
                btnSaveActivity.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            } else if (count == 0 && start == 0) {
                btnSaveActivity.setBackgroundColor(resources.getColor(R.color.disabled))
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
                        .setWallet("My Wallet")
                        .update()
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
            if (activityType == Record.EXPENSE) activityType = Record.INCOME else activityType = Record.EXPENSE
            refreshActivityType()
        }
    }

    private fun refreshActivityType() {
        if (activityType == Record.INCOME) {
            tvTypeIncome.setBackgroundColor(resources.getColor(R.color.colorAccent))
            tvTypeExpense.setBackgroundColor(resources.getColor(R.color.unselected))
        } else {
            tvTypeIncome.setBackgroundColor(resources.getColor(R.color.unselected))
            tvTypeExpense.setBackgroundColor(resources.getColor(R.color.colorAccent))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            inActivityValue.setText(PublicMethods.moneyFormat(data!!.getLongExtra("value", 0).toString()))
        }
    }
}
