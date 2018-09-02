package com.simpleapp.savi

import android.app.Application
import com.simpleapp.savi.model.Wallet
import io.realm.Realm
import io.realm.RealmConfiguration

class SaviApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
                .name("savy")
                .schemaVersion(1)
                .build()
        Realm.setDefaultConfiguration(configuration)
        initWallet(Realm.getInstance(configuration))
    }

    private fun initWallet(realm: Realm) {
        if (realm.where(Wallet::class.java).equalTo("name", "My Wallet").findFirst() == null) {
            realm.executeTransaction{
                realm.createObject(Wallet::class.java, "My Wallet")
            }
        }
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }
}