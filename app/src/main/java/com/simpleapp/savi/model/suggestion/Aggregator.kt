package com.simpleapp.savi.model.suggestion

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.Sort

class Aggregator {

    companion object {
        val UPDATE_DECREASE: Byte = 0
        val UPDATE_INCREASE: Byte = 1
        val FREQUENCY_1: Byte = 0
        val FREQUENCY_2: Byte = 1
        val FREQUENCY_3: Byte = 2
        val FREQUENCY_4: Byte = 3
        val FREQUENCY_5: Byte = 4

        fun inc(name: String, value: Long, isRealmOpened: Boolean = false) {
            val realm = Realm.getDefaultInstance()
            val firstCharAscii = name[0].toInt().toByte()
            when ((firstCharAscii % 5).toByte()) {
                FREQUENCY_1 -> {
                    val result = realm.where(NameFrequency_1::class.java).equalTo("name", name).findFirst()
                    if (result == null) (new(realm, name, FREQUENCY_1, isRealmOpened)) else {update(realm, result, UPDATE_INCREASE, isRealmOpened)}
                }
                FREQUENCY_2 -> {
                    val result = realm.where(NameFrequency_2::class.java).equalTo("name", name).findFirst()
                    if (result == null) (new(realm, name, FREQUENCY_2, isRealmOpened)) else {update(realm, result, UPDATE_INCREASE, isRealmOpened)}
                }
                FREQUENCY_3 -> {
                    val result = realm.where(NameFrequency_3::class.java).equalTo("name", name).findFirst()
                    if (result == null) (new(realm, name, FREQUENCY_3, isRealmOpened)) else {update(realm, result, UPDATE_INCREASE, isRealmOpened)}
                }
                FREQUENCY_4 -> {
                    val result = realm.where(NameFrequency_4::class.java).equalTo("name", name).findFirst()
                    if (result == null) (new(realm, name, FREQUENCY_4, isRealmOpened)) else {update(realm, result, UPDATE_INCREASE, isRealmOpened)}
                }
                FREQUENCY_5 -> {
                    val result = realm.where(NameFrequency_5::class.java).equalTo("name", name).findFirst()
                    if (result == null) (new(realm, name, FREQUENCY_5, isRealmOpened)) else {update(realm, result, UPDATE_INCREASE, isRealmOpened)}
                }
            }

            val realmTransaction = {
                var valFrequency = realm.where(ValueFrequency::class.java)
                        .equalTo("name", name).and()
                        .equalTo("value", value)
                        .findFirst()
                if (valFrequency == null) {
                    valFrequency = realm.createObject(ValueFrequency::class.java)
                    valFrequency.name = name
                    valFrequency.value = value
                    valFrequency.frequency = 1
                } else {
                    valFrequency.frequency++
                }
            }

            if (isRealmOpened)  {
                realmTransaction()
            } else {
                realm.executeTransaction{
                    realmTransaction()
                }
            }
        }

        fun dec(name: String, value : Long, isRealmOpened: Boolean = false) {
            val realm = Realm.getDefaultInstance()
            val firstCharAscii = name[0].toInt().toByte()
            when ((firstCharAscii % 5).toByte()) {
                FREQUENCY_1 -> {
                    val result = realm.where(NameFrequency_1::class.java).equalTo("name", name).findFirst()
                    if (result == null) (throw AggregationException.INVALID_DEC) else {update(realm, result, UPDATE_DECREASE, isRealmOpened)}
                }
                FREQUENCY_2 -> {
                    val result = realm.where(NameFrequency_2::class.java).equalTo("name", name).findFirst()
                    if (result == null) (throw AggregationException.INVALID_DEC) else {update(realm, result, UPDATE_DECREASE, isRealmOpened)}
                }
                FREQUENCY_3 -> {
                    val result = realm.where(NameFrequency_3::class.java).equalTo("name", name).findFirst()
                    if (result == null) (throw AggregationException.INVALID_DEC) else {update(realm, result, UPDATE_DECREASE, isRealmOpened)}
                }
                FREQUENCY_4 -> {
                    val result = realm.where(NameFrequency_4::class.java).equalTo("name", name).findFirst()
                    if (result == null) (throw AggregationException.INVALID_DEC) else {update(realm, result, UPDATE_DECREASE, isRealmOpened)}
                }
                FREQUENCY_5 -> {
                    val result = realm.where(NameFrequency_5::class.java).equalTo("name", name).findFirst()
                    if (result == null) (throw AggregationException.INVALID_DEC) else {update(realm, result, UPDATE_DECREASE, isRealmOpened)}
                }
            }

            val realmTransaction = {
                val valFrequency = realm.where(ValueFrequency::class.java)
                        .equalTo("name", name).and()
                        .equalTo("value", value)
                        .findFirst()
                if (valFrequency == null) {
                    throw AggregationException.INVALID_DEC
                } else {
                    if (valFrequency.frequency-1 == 0) {
                        valFrequency.deleteFromRealm()
                    } else {
                        valFrequency.frequency--
                    }
                }
            }

            if (isRealmOpened) {
                realmTransaction()
            } else {
                realm.executeTransaction{
                    realmTransaction()
                }
            }
        }

        private fun new(realm: Realm, name: String, modelType: Byte, isRealmOpened: Boolean = false) {
            val realmTransaction = {
                when (modelType) {
                    FREQUENCY_1 -> realm.createObject(NameFrequency_1::class.java, name).frequency = 1
                    FREQUENCY_2 -> realm.createObject(NameFrequency_2::class.java, name).frequency = 1
                    FREQUENCY_3 -> realm.createObject(NameFrequency_3::class.java, name).frequency = 1
                    FREQUENCY_4 -> realm.createObject(NameFrequency_4::class.java, name).frequency = 1
                    FREQUENCY_5 -> realm.createObject(NameFrequency_5::class.java, name).frequency = 1
                }
            }
            if (isRealmOpened) {
                realmTransaction()
            } else {
                realm.executeTransaction{
                    realmTransaction()
                }
            }
        }

        private fun <T: NameFrequencyAccessor> update(realm: Realm, model: T, updateType: Byte, isRealmOpened: Boolean = false) {
            val realmTransaction = {
                if (updateType == UPDATE_DECREASE) {
                    if (model.grabFrequency()-1 == 0) {
                        (model as RealmObject).deleteFromRealm()
                    } else {
                        model.updateFrequency(model.grabFrequency()-1)
                    }
                } else {
                    model.updateFrequency(model.grabFrequency()+1)
                }
            }

            if (isRealmOpened) {
                realmTransaction()
            } else {
                realm.executeTransaction {
                    realmTransaction()
                }
            }
        }

        fun valueOf(name : String) : Long {
            val realm = Realm.getDefaultInstance()
            val result = realm.where(ValueFrequency::class.java)
                    .equalTo("name", name)
                    .sort("frequency", Sort.DESCENDING)
                    .findFirst()
            return result!!.value
        }

        fun aggregate(substring: String) : ArrayList<NameFrequency>? {
            if (substring.length == 0) {
                return null
            }
            val firstCharAscii = substring[0].toUpperCase().toInt().toByte()
            val realm = Realm.getDefaultInstance()
            val results : ArrayList<NameFrequency>?
            when ((firstCharAscii % 5).toByte()) {
                FREQUENCY_1 -> results = generalize(realm.where(NameFrequency_1::class.java)
                        .sort("frequency", Sort.DESCENDING).findAll())
                FREQUENCY_2 -> results = generalize(realm.where(NameFrequency_2::class.java)
                        .sort("frequency", Sort.DESCENDING).findAll())
                FREQUENCY_3 -> results = generalize(realm.where(NameFrequency_3::class.java)
                        .sort("frequency", Sort.DESCENDING).findAll())
                FREQUENCY_4 -> results = generalize(realm.where(NameFrequency_4::class.java)
                        .sort("frequency", Sort.DESCENDING).findAll())
                FREQUENCY_5 -> results = generalize(realm.where(NameFrequency_5::class.java)
                        .sort("frequency", Sort.DESCENDING).findAll())
                else -> results = null
            }
            if (results == null) {
                return null
            } else {
                return results
            }
        }

        private fun <T : NameFrequencyAccessor> generalize(results: RealmResults<T>) : ArrayList<NameFrequency> {
            val res = ArrayList<NameFrequency>()
            results.mapTo(res) { NameFrequency(it.grabName(), it.grabFrequency()) }
            return res
        }

        class AggregationException(override val message : String) : Exception(message) {
            companion object {
                val INVALID_DEC = AggregationException("Decreasing non-available frequency name")
            }
        }
    }
}