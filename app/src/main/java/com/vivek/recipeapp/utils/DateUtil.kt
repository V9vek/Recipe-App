package com.vivek.recipeapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    // Ex: November 4, 2021
    private val sdf = SimpleDateFormat("MMMMM d, yyyy")

    fun longToDate(long: Long): Date {
        return Date(long)
    }

    fun dateToLong(date: Date): Long {
        return date.time / 1000         // return seconds
    }

    fun dateToString(date: Date): String {
        return sdf.format(date)
    }

    fun stringToDate(string: String): Date {
        return sdf.parse(string)
            ?: throw NullPointerException("Could not convert date string to Date object.")
    }

    fun createTimestamp(): Date {
        return Date()
    }
}









