package com.simpleapp.savi.model.record

import com.simpleapp.savi.TransactionManager
import com.simpleapp.savi.model.Date
import com.simpleapp.savi.model.Wallet
import io.realm.Realm
import java.util.*

class DailyRecord(datestamp: Int, val wallet: Wallet) {
    val date: Date = Date(datestamp)
    var income: Long = 0
    var expense: Long = 0
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

    fun saveNewActivity(name: String, value: Long, notes: String, type: Byte) {
        TransactionManager.new(name, value, notes, type, wallet, date.toDatestamp())
        when(type) {
            Record.INCOME -> income += value
            Record.EXPENSE -> expense += value
        }
    }
}