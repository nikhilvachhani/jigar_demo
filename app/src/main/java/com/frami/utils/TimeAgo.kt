package com.frami.utils

import java.util.*
import java.util.concurrent.TimeUnit

object TimeAgo {
    val times = Arrays.asList(
        TimeUnit.DAYS.toMillis(365),
        TimeUnit.DAYS.toMillis(30),
        TimeUnit.DAYS.toMillis(1),
        TimeUnit.HOURS.toMillis(1),
        TimeUnit.MINUTES.toMillis(1),
        TimeUnit.SECONDS.toMillis(1)
    )
    val timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second")
    fun toDuration(duration: Long): String {
        val res = StringBuffer()
        for (i in times.indices) {
            val current = times[i]
            val temp = duration / current
            if (temp > 0) {
                res.append(temp).append(" ").append(timesString[i])
                    .append(if (temp != 1L) "s" else "").append(" ago")
                break
            }
        }
        return if ("" == res.toString()) "0 seconds ago" else res.toString()
    }
//
//    @JvmStatic
//    fun main(args: Array<String>) {
//        println(toDuration(123))
//        println(toDuration(1230))
//        println(toDuration(12300))
//        println(toDuration(123000))
//        println(toDuration(1230000))
//        println(toDuration(12300000))
//        println(toDuration(123000000))
//        println(toDuration(1230000000))
//        println(toDuration(12300000000L))
//        println(toDuration(123000000000L))
//    }
}