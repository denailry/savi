package com.simpleapp.savy.lib

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
    }
}