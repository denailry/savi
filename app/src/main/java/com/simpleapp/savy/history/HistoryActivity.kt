package com.simpleapp.savy.history

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.simpleapp.savy.model.record.DailyRecord
import com.simpleapp.savy.R
import com.simpleapp.savy.recordedit.RecordEditActivity
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
    }

    private fun showHistory() {
        val datestamp = this.intent.getIntExtra("datestamp", -1)
        if (datestamp == -1) {
            Toast.makeText(this, "Cannot get datestamp", Toast.LENGTH_SHORT).show()
        } else {
            val records = DailyRecord(datestamp)
            rvHistory.layoutManager = LinearLayoutManager(this)
            rvHistory.adapter = HistoryAdapter(records.recordList, this, object : HistoryItemClickListener {
                override fun onClick(id: Long) {
                    val intent = Intent(applicationContext, RecordEditActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        showHistory()
    }
}
