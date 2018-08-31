package com.simpleapp.savy

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class SavyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
                .name("savy")
                .schemaVersion(1)
                .build()
        Realm.setDefaultConfiguration(configuration)
        Realm.getInstance(configuration)
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }
}