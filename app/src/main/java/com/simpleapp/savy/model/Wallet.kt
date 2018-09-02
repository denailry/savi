package com.simpleapp.savy.model

import com.simpleapp.savy.model.record.DailyRecord
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Wallet(
        @PrimaryKey open var name: String = "",
        open var balance: Long = 0
) : RealmObject() {
    companion object {
        fun new(name: String) : Wallet {
            val realm = Realm.getDefaultInstance()
            var obj: Wallet? = realm.where(Wallet::class.java).equalTo("name", name).findFirst()
            if (obj != null) {
                throw WalletException("Wallet's name has been existed")
            }
            realm.executeTransaction{
                obj = realm.createObject(Wallet::class.java, name)
            }
            return obj!!
        }
        fun getWallet(name: String) : Wallet {
            val realm = Realm.getDefaultInstance()
            val wallet = realm.where(Wallet::class.java).equalTo("name", name).findFirst()
            if (wallet == null) {
                throw WalletException("Wallet's name does not exist")
            }
            return wallet
        }
    }

    fun getRecords(datestamp: Int) : DailyRecord {
        return DailyRecord(datestamp, this)
    }

    data class WalletException(override val message: String) : Exception()
}