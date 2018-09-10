package com.simpleapp.savi.lib

import android.content.Context
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.util.DisplayMetrics
import android.util.Log
import java.util.*


class PublicMethods {
    companion object {
        fun moneyFormat(balance: String): String {
            val arrc = balance.toCharArray()
            val money = CharArray(arrc.size + (arrc.size - 1) / 3)
            var i = 0
            var j = (arrc.size - 1) / 3
            while (i < arrc.size) {
                if (i != 0 && i % 3 == 0) {
                    money[arrc.size - 1 - i + j] = '.'
                    j--
                }
                money[arrc.size - 1 - i + j] = arrc[arrc.size - 1 - i]
                i++
            }
            return String(money)
        }

        fun setLocale(language: String, context: Context) {
            var code = "en"
            if (language == "Indonesia") {
                code = "in"
            }
            Log.d("TEST", "locale is set to " + code)
            val myLocale = Locale(code)
            val res = context.resources
            val dm = res.getDisplayMetrics()
            val conf = res.getConfiguration()
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
        }
    }
}