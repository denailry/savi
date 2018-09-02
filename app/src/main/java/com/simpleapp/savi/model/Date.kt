package com.simpleapp.savi.model

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

        fun MonthName(monthNumber: Int) : String? {
            when(monthNumber) {
                0 -> return "January"
                1 -> return "February"
                2 -> return "March"
                3 -> return "April"
                4 -> return "May"
                5 -> return "June"
                6 -> return "July"
                7 -> return "August"
                8 -> return "September"
                9 -> return "October"
                10 -> return "November"
                11 -> return "December"
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

    fun getDayName() : String? {
        val calendar = GregorianCalendar(year, month, day)
        when(calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> return "Monday"
            Calendar.TUESDAY -> return "Tuesday"
            Calendar.WEDNESDAY -> return "Wednesday"
            Calendar.THURSDAY -> return "Thursday"
            Calendar.FRIDAY -> return "Friday"
            Calendar.SATURDAY -> return "Saturday"
            Calendar.SUNDAY -> return "Sunday"
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