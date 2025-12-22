package com.kloubit.gps.infrastructure.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DifferenceBetweenDatesUtils {
    companion object {
        fun getDifferenceBetweenDates(scheduleTimestamp: Long, currentTimestamp: Long): String {
            return try {
                val diffInMillis = currentTimestamp - scheduleTimestamp
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                minutes.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        fun longToHour(timeInMillis: Long): String {
            val date = Date(timeInMillis)
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(date)
        }
    }


}