package com.simpleapp.savi

import android.util.Log
import com.simpleapp.savi.model.Wallet
import com.simpleapp.savi.model.record.Record
import com.simpleapp.savi.model.suggestion.Aggregator
import io.realm.Realm

class TransactionManager {
    companion object {
        fun new(name: String, value: Long, notes: String, type: Byte, wallet: Wallet, datestamp: Int) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction{
                val record = realm.createObject(Record::class.java, Record.getNewId())
                record.datestamp = datestamp
                record.name = name
                record.value = value
                record.notes = notes
                record.type = type
                record.wallet = wallet.name
                if (type == Record.INCOME) {
                    try {
                        wallet.balance += value
                    } catch (e: Exception) {
                        throw TransactionException("Oops, wallet is not large enough")
                    }
                } else {
                    try {
                        wallet.balance -= value
                    } catch (e: Exception) {
                        throw TransactionException("Oops, wallet is not large enough")
                    }
                }
                Aggregator.inc(name, value, true)
            }
        }

        fun delete(record: Record) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction{
                Aggregator.dec(record.name, record.value, true)
                if (record.type == Record.INCOME) {
                    try {
                        Wallet.getWallet(record.wallet).balance -= record.value
                    } catch (e: Exception) {
                        throw TransactionException("Oops, wallet is not large enough")
                    }
                } else {
                    try {
                        Wallet.getWallet(record.wallet).balance += record.value
                    } catch (e: Exception) {
                        Log.d("TEST", e.toString())
                        throw TransactionException("Oops, wallet is not large enough")
                    }
                }
                record.deleteFromRealm()
            }
        }
    }

    class Updater(val record: Record) {
        private var datestamp: Int? = null
        private var wallet: String? = null
        private var name: String? = null
        private var value: Long? = null
        private var notes: String? = null
        private var type: Byte? = null

        fun setDatestamp(datestamp: Int) : Updater {
            this.datestamp = datestamp
            return this
        }
        fun setWallet(wallet: String) : Updater {
            this.wallet = wallet
            return this
        }
        fun setName(name: String) : Updater {
            this.name = name
            return this
        }
        fun setValue(value: Long) : Updater {
            this.value = value
            return this
        }
        fun setNotes(notes: String) : Updater {
            this.notes = notes
            return this
        }
        fun setType(type: Byte) : Updater {
            this.type = type
            return this
        }

        fun update() {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction{
                try {
                    if (wallet != null) {
                        val prevValue = record.value
                        val newValue = if (value == null) record.value else value!!
                        val prevType = record.type
                        val newType = if (type == null) record.type else type
                        val source = Wallet.getWallet(record.wallet)
                        val dest = Wallet.getWallet(wallet!!)
                        if (prevType == Record.INCOME) source.balance -= prevValue else source.balance += prevValue
                        if (newType == Record.INCOME) dest.balance += newValue else dest.balance -= newValue
                    }
                } catch (e: Exception) {
                    throw TransactionException("Oops, wallet is not large enough")
                }
                if (datestamp != null) record.datestamp = datestamp!!
                if (notes != null) record.notes = notes!!
                if (type != null) record.type = type!!
                if (name != null || value != null) {
                    Aggregator.dec(record.name, record.value, true)
                    if (name != null) record.name = name!!
                    if (value != null) record.value = value!!
                    Aggregator.inc(record.name, record.value, true)
                }
            }
        }
    }

    data class TransactionException(override val message: String) : Exception()
}