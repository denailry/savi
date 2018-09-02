package com.simpleapp.savy.model.record

import com.simpleapp.savy.model.Wallet
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Record(
        @PrimaryKey open var id: Long = 0,
        open var datestamp: Int = 0,
        open var wallet: String = "",
        open var name: String = "",
        open var value: Long = 0,
        open var notes: String = "",
        open var type: Byte = 0
) : RealmObject() {
    companion object {
        val INCOME : Byte = 0
        val EXPENSE : Byte = 1
        fun getNewId() : Long {
            return IDGenerator.getInstance().generate()
        }
    }

    class IDGenerator {
        var currentId = -1L

        companion object {
            var generator: IDGenerator? = null
            fun getInstance() : IDGenerator {
                if (generator == null) {
                    generator = IDGenerator()
                }
                return generator!!
            }
        }

        init {
            val realm = Realm.getDefaultInstance()
            val record = realm.where(Record::class.java).findFirst()
            if (record != null) {
                currentId = realm.where(Record::class.java).max("id") as Long
            }
        }

        fun generate() : Long {
            currentId++
            return currentId
        }
    }
}