package com.simpleapp.savy.recordedit

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.simpleapp.savy.R
import com.simpleapp.savy.calculator.CalculatorActivity
import com.simpleapp.savy.model.record.Record
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_record_edit.*

class RecordEditActivity : AppCompatActivity() {

    private var activityType = Record.EXPENSE
    private var record: Record? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_edit)
        initialize()
        setupClickListener()
    }

    private fun initialize() {
        val realm = Realm.getDefaultInstance()
        record = realm.where(Record::class.java)
                .equalTo("id", intent.getLongExtra("id", 0L))
                .findFirst()
        inActivityName.setText(record!!.name)
        inActivityValue.setText(record!!.value.toString())
        inActivityNote.setText(record!!.notes)
        activityType = record!!.type
    }

    private fun setupClickListener() {
        btnSaveActivity.setOnClickListener{_ ->
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction{
                record!!.name = inActivityName.text.toString()
                record!!.value = inActivityValue.text.toString().toInt()
                record!!.notes = inActivityNote.text.toString()
                record!!.type = activityType
            }
            finish()
        }
        btnDeleteActivity.setOnClickListener{_ ->
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction{
                record!!.deleteFromRealm()
            }
            finish()
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
        viewActivityType.setOnClickListener { _ ->
            if (activityType == Record.EXPENSE) {
                activityType = Record.INCOME
            } else {
                activityType = Record.EXPENSE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inActivityValue.setText(data!!.getIntExtra("value", 0).toString())
    }
}
