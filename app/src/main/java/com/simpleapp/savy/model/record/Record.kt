package com.simpleapp.savy.model.record

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Record(
        @PrimaryKey open var id: Long = 0,
        open var datestamp: Int = 0,
        open var name: String = "",
        open var value: Int = 0,
        open var notes: String = "",
        open var type: Byte = 0
) : RealmObject() {

    companion object {
        val INCOME : Byte = 0
        val EXPENSE : Byte = 1
    }
}