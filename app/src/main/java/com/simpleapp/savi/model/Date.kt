package com.simpleapp.savi.model

import android.content.Context
import com.simpleapp.savi.R
import java.util.*

class Date(datestamp: Int? = null) {
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0

    constructor(d: Int, m: Int, y: Int) : this(null) {
        day = d
        month = m
        year = y
    }

    init {
        if (datestamp != null) {
            year = datestamp.rem(10000)
            month = datestamp.div(10000).rem(100)
            day = datestamp.div(1000000)
        }
    }

    companion object {

        fun MonthName(context: Context, monthNumber: Int) : String? {
            when(monthNumber) {
                0 -> return context.resources.getString(R.string.january)
                1 -> return context.resources.getString(R.string.february)
                2 -> return context.resources.getString(R.string.march)
                3 -> return context.resources.getString(R.string.april)
                4 -> return context.resources.getString(R.string.may)
                5 -> return context.resources.getString(R.string.june)
                6 -> return context.resources.getString(R.string.july)
                7 -> return context.resources.getString(R.string.august)
                8 -> return context.resources.getString(R.string.september)
                9 -> return context.resources.getString(R.string.october)
                10 -> return context.resources.getString(R.string.november)
                11 -> return context.resources.getString(R.string.december)
            }
            return null
        }

        fun Today() : Date {
            val date = Date()
            date.parseCalendar(java.util.Calendar.getInstance())
            return date
        }
    }

    fun parseCalendar(calendar : Calendar) {
        this.day = calendar.get(Calendar.DAY_OF_MONTH)
        this.month = calendar.get(Calendar.MONTH)
        this.year = calendar.get(Calendar.YEAR)
    }

    fun getDayName(context: Context) : String? {
        val calendar = GregorianCalendar(year, month, day)
        when(calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> return context.resources.getString(R.string.monday)
            Calendar.TUESDAY -> return context.resources.getString(R.string.tuesday)
            Calendar.WEDNESDAY -> return context.resources.getString(R.string.wednesday)
            Calendar.THURSDAY -> return context.resources.getString(R.string.thursday)
            Calendar.FRIDAY -> return context.resources.getString(R.string.friday)
            Calendar.SATURDAY -> return context.resources.getString(R.string.saturday)
            Calendar.SUNDAY -> return context.resources.getString(R.string.sunday)
        }
        return null
    }

    fun toDatestamp() : Int {
        return (day * 1000000) + (month * 10000) + year
    }

    fun tommorow() : Date {
        val newCalendar = GregorianCalendar.getInstance()
        newCalendar.timeInMillis = this.toMilliSeconds() + 86400000
        val newDate = Date()
        newDate.parseCalendar(newCalendar)
        return newDate
    }

    fun yesterday() : Date {
        val newCalendar = GregorianCalendar.getInstance()
        newCalendar.timeInMillis = this.toMilliSeconds() - 86400000
        val newDate = Date()
        newDate.parseCalendar(newCalendar)
        return newDate
    }

    fun toMilliSeconds() : Long {
        val calendar = GregorianCalendar(year, month, day)
        return calendar.timeInMillis
    }

    override fun toString() : String {
        var sDay = day.toString()
        val trueMonth = month + 1
        var sMonth = trueMonth.toString()
        if (day < 10) {
            sDay = "0" + sDay
        }
        if (trueMonth < 10) {
            sMonth = "0" + sMonth
        }
        return sDay + "-" + sMonth + "-" + year.toString()
    }
}