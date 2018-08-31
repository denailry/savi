package com.simpleapp.savy.model.record

import com.simpleapp.savy.model.Date
import io.realm.Realm
import java.util.*

class DailyRecord(datestamp: Int) {
    val date: Date = Date(datestamp)
    var income: Int = 0
    var expense: Int = 0
    val recordList: ArrayList<Record> = ArrayList()

    init {
        val realm = Realm.getDefaultInstance()
        val results = realm.where(Record::class.java)
                .equalTo("datestamp", datestamp)
                .findAll()
        for (result in results) {
            if (result.type == Record.EXPENSE) {
                expense += result.value
            } else {
                income += result.value
            }
            recordList.add(result)
        }
    }

    fun saveNewActivity(name: String, value: Int, notes: String, type: Byte) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction{
            val millis = Calendar.getInstance().timeInMillis
            val record = realm.createObject(Record::class.java, millis)
            record.datestamp = date.toDatestamp()
            record.name = name
            record.value = value
            record.notes = notes
            when(type) {
                Record.INCOME -> record.type = 0
                Record.EXPENSE -> record.type = 1
            }
        }
        when(type) {
            Record.INCOME -> income += value
            Record.EXPENSE -> expense += value
        }
    }
}