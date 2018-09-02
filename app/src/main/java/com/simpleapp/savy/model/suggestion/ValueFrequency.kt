package com.simpleapp.savy.model.suggestion

import io.realm.RealmObject

open class ValueFrequency(
        open var name: String = "",
        open var value: Long = 0,
        open var frequency: Int = 0
) : RealmObject() {
}
