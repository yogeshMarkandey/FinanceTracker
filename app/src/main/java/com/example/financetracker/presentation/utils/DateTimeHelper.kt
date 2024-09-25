package com.example.financetracker.presentation.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateTimeHelper {
    companion object {
        private val TAG = this::class.java.name
        fun format(date: Date, pattern: String): String {
            try {
                val format = SimpleDateFormat(pattern, Locale.ENGLISH)
                return format.format(date)
            } catch (e: Exception) {
                Log.e(TAG, "format: Error formatting date : pattern: $pattern", e)
                return "Error"
            }
        }

        fun getWeekStartAndEnd(initialDate: Date? = null, offset: Int = 0): Pair<Date, Date> {
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.MONDAY

            if (initialDate != null) {
                calendar.time = initialDate
            }

            calendar.add(Calendar.WEEK_OF_YEAR, offset)

            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            val startOfWeek = calendar.time

            calendar.add(Calendar.DAY_OF_WEEK, 6)
            val endOfWeek = calendar.time

            return Pair(startOfWeek, endOfWeek)
        }

        fun getDayStartAndEnd(initialDate: Date? = null, offset: Int = 0): Pair<Date, Date> {
            val calendar = Calendar.getInstance()

            if (initialDate != null) {
                calendar.time = initialDate
            }

            calendar.add(Calendar.DAY_OF_YEAR, offset)
            resetStartTime(calendar)
            val startOfDay = calendar.time

            resetEndTime(calendar)
            val endOfDay = calendar.time

            return Pair(startOfDay, endOfDay)
        }

        fun getMonthStartAndEnd(initialDate: Date? = null, offset: Int = 0): Pair<Date, Date> {
            val calendar = Calendar.getInstance()

            if (initialDate != null) {
                calendar.time = initialDate
            }

            calendar.add(Calendar.MONTH, offset)

            calendar.set(Calendar.DAY_OF_MONTH, 1)
            resetStartTime(calendar)
            val startOfMonth = calendar.time

            val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            calendar.set(Calendar.DAY_OF_MONTH, lastDay)
            resetEndTime(calendar)
            val endOfMonth = calendar.time

            return Pair(startOfMonth, endOfMonth)
        }

        fun getYearStartAndEnd(initialDate: Date? = null, offset: Int = 0): Pair<Date, Date> {
            val calendar = Calendar.getInstance()

            if (initialDate != null) {
                calendar.time = initialDate
            }

            calendar.add(Calendar.YEAR, offset)

            calendar.set(Calendar.DAY_OF_YEAR, 1)
            resetStartTime(calendar)
            val startOfYear = calendar.time

            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            resetEndTime(calendar)
            val endOfYear = calendar.time

            return Pair(startOfYear, endOfYear)
        }

        fun resetStartTime(calendar: Calendar) {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }

        fun resetEndTime(calendar: Calendar) {
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
        }
    }
}