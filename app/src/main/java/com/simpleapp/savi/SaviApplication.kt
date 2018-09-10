package com.simpleapp.savi

import android.app.Application
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import com.simpleapp.savi.lib.PublicMethods
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
        initPreference()
    }

    private fun initWallet(realm: Realm) {
        if (realm.where(Wallet::class.java).equalTo("name", "My Wallet").findFirst() == null) {
            realm.executeTransaction{
                realm.createObject(Wallet::class.java, "My Wallet")
            }
        }
    }

    private fun initPreference() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var language = sharedPreferences.getString("language", "")
        if (language.isEmpty()) {
            val edit = sharedPreferences.edit()
            edit.putString("language", "English")
            edit.apply()
            language = "English"
        }
        PublicMethods.setLocale(language, this)
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }
}