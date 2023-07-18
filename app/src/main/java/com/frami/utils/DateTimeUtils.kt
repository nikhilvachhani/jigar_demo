package com.frami.utils

import android.text.TextUtils
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    var ddMMMMyyyy: String = "dd MMMM yyyy"
    var EEEddMMMyyyy: String = "EEE dd MMM yyyy"
    var yyyyMMdd: String = "yyyy-MM-dd"
    var yyyyMMddHHmmss: String = "yyyy-MM-dd HH:mm:ss"
    var hhmma: String = "hh:mm a"
    var HHmm: String = "HH:mm"
    var date12Format = SimpleDateFormat("hh:mm a", Locale.US)
    var date24Format = SimpleDateFormat("HH:mm", Locale.US)
    var dateFormatDD = SimpleDateFormat("dd", Locale.US)
    var dateFormatMM = SimpleDateFormat("MM", Locale.US)
    var dateFormatYYYY = SimpleDateFormat("yyyy", Locale.US)
    var dateFormatMMMYY = SimpleDateFormat("MMM-yy", Locale.US)
    var dateFormatE = SimpleDateFormat("E", Locale.US)
    var dateFormatDMMM = SimpleDateFormat("d-MMM", Locale.US)
    var dateFormatYYYYMMDD: DateFormat = SimpleDateFormat("yyyy MM dd", Locale.US)
    var dateFormatYYYY_MM_DD: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var dateFormatYYYYMMDD_HH_MM_A: DateFormat =
        SimpleDateFormat("yyyy MM dd hh:mm a", Locale.US)
    var dateFormatDDMMMYYYY_HHMMA: DateFormat =
        SimpleDateFormat("dd MMM, yyyy HH:mm", Locale.US)
    var dateFormatDDMMMMYYYY: DateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.US)
    var dateFormatDDMMMMYYYYhhmma: DateFormat = SimpleDateFormat("dd MMMM, yyyy hh:mm a", Locale.US)
    var dateFormatMMMDD: DateFormat = SimpleDateFormat("MMM dd", Locale.US)
    var dateFormatMMMDDYYYY: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    var dateFormatServer_YYYY_MM_DD_T_HHMMSS: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    var dateFormatServer_YYYY_MM_DD_T_HHMMSSZ: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
    var dateFormatMMM_DD_YYYY_HH_MM: DateFormat =
        SimpleDateFormat("MMM, dd yyyy 'at' HH:mm", Locale.US)
    var dateFormatTODAYYESTERDAY_AT_HH_MM: DateFormat =
        SimpleDateFormat(" 'at' HH:mm", Locale.US)
    var dateFormat_DD_MM_YYYY_HH_MM: DateFormat =
        SimpleDateFormat("dd MM yyyy 'at' HH:mm", Locale.US)
    var dateFormat_DD_MMM_YYYY_HH_MM: DateFormat =
        SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.US)
    var dateFormatDefault: DateFormat = DateFormat.getDateTimeInstance()

    fun convertDateFormat(
        date: String,
        sourceStr: String,
        destinationStr: String
    ): String {
        var strNewDate = date
        val newDate: Date
        val source = SimpleDateFormat(sourceStr, Locale.US)
        val destination = SimpleDateFormat(destinationStr, Locale.US)
        try {
            if (!TextUtils.isEmpty(date)) {
                newDate = source.parse(date)
                strNewDate = destination.format(newDate)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return strNewDate
    }

    fun getCurrentTime(
        days: Int = 0,
        dayMonthYear: AppConstants.DURATION = AppConstants.DURATION.WEEKLY
    ): String {
        val c: Calendar = Calendar.getInstance()
        if (dayMonthYear == AppConstants.DURATION.WEEKLY) {
            c.add(Calendar.DAY_OF_YEAR, days)
        } else if (dayMonthYear == AppConstants.DURATION.MONTHLY) {
            c.add(Calendar.MONTH, days)
        } else if (dayMonthYear == AppConstants.DURATION.YEARLY) {
            c.add(Calendar.YEAR, days)
        }
        val df = SimpleDateFormat(yyyyMMddHHmmss)
        val formattedDate: String = dateFormatServer_YYYY_MM_DD_T_HHMMSS.format(c.time)
        return formattedDate
    }

    fun isTimeAfter(startTime: Date?, endTime: Date): Boolean {
        return !endTime.before(startTime)
    }

    fun getDateFromDate(strDate: String, dateFormat: DateFormat): String {
        var convertedDate = Date()
        try {
            convertedDate = dateFormatYYYYMMDD.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        if (convertedDate != null) {
            calendar.time = convertedDate
        }
        return dateFormat.format(calendar.time)
    }

    fun getCalendarFromDate(strDate: String): Calendar {
        var convertedDate = Date()
        try {
            convertedDate = dateFormatServer_YYYY_MM_DD_T_HHMMSS.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        if (convertedDate != null) {
            calendar.time = convertedDate
        }
        return calendar
    }

    fun getDateFromDateFormatToTODateFormat(
        strDate: String,
        dateFormatFrom: DateFormat,
        dateFormatTo: DateFormat
    ): String {
        var convertedDate = Date()
//        dateFormatFrom.timeZone = TimeZone.getTimeZone("UTC")
//        dateFormatTo.timeZone = TimeZone.getDefault()
        try {
            convertedDate = dateFormatFrom.parse(strDate) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return dateFormatTo.format(calendar.time)
    }

    fun getLocalDateFromDateFormatToTODateFormat(
        strDate: String,
        dateFormatFrom: DateFormat,
        dateFormatTo: DateFormat
    ): String {
        var convertedDate = Date()
        dateFormatFrom.timeZone = TimeZone.getDefault()
        dateFormatTo.timeZone = TimeZone.getDefault()
        try {
            convertedDate = dateFormatFrom.parse(strDate) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return dateFormatTo.format(calendar.time)
    }

    fun getDateLocalTOUTCDateFormat(
        strDate: String,
        dateFormatFrom: DateFormat,
        dateFormatTo: DateFormat
    ): String {
        var convertedDate = Date()
        dateFormatFrom.timeZone = TimeZone.getDefault()
        dateFormatTo.timeZone = TimeZone.getTimeZone("UTC")
        try {
            convertedDate = dateFormatFrom.parse(strDate) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return dateFormatTo.format(calendar.time)
    }

    fun getDateUTCToLocalFormat(
        strDate: String,
        dateFormatFrom: DateFormat,
        dateFormatTo: DateFormat
    ): String {
        var convertedDate = Date()
        dateFormatFrom.timeZone = TimeZone.getTimeZone("UTC")
        dateFormatTo.timeZone = TimeZone.getDefault()
        try {
            convertedDate = dateFormatFrom.parse(strDate) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return dateFormatTo.format(calendar.time)
    }

    fun getDateFromDateFormatToTODateFormatWithUTCtoLOCAL(
        strDate: String,
        dateFormatFrom: DateFormat,
        dateFormatTo: DateFormat
    ): String {
        var convertedDate = Date()
        dateFormatFrom.timeZone = TimeZone.getTimeZone("UTC")
        dateFormatTo.timeZone = TimeZone.getDefault()
        try {
            convertedDate = dateFormatFrom.parse(strDate) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return dateFormatTo.format(calendar.time)
    }

    fun getDateFromServerDateFormatToTODateFormat(
        strDate: String,
        dateFormatTo: DateFormat
    ): String {
        return getDateFromDateFormatToTODateFormat(
            strDate,
            dateFormatServer_YYYY_MM_DD_T_HHMMSS,
            dateFormatTo
        )
    }

    fun getDateFromServerUTCDateFormatToTOLocalDateFormat(
        strDate: String,
        dateFormatTo: DateFormat
    ): String {
        return getDateUTCToLocalFormat(
            strDate,
            dateFormatServer_YYYY_MM_DD_T_HHMMSS,
            dateFormatTo
        )
    }

    fun getDateFromServerDateFormatToTODateFormatUTCtoLOCAL(
        strDate: String,
        dateFormatTo: DateFormat
    ): String {
        return getDateFromDateFormatToTODateFormatWithUTCtoLOCAL(
            strDate,
            dateFormatServer_YYYY_MM_DD_T_HHMMSS,
            dateFormatTo
        )
    }

    fun getServerDateFromDateFormat(
        strDate: String
    ): Date {
        var convertedDate = Date()
        try {
            convertedDate = dateFormatServer_YYYY_MM_DD_T_HHMMSS.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return calendar.time
    }

    fun getServerCalendarDateFromDateFormat(
        strDate: String
    ): Calendar {
        var convertedDate = Date()
        try {
            convertedDate = dateFormatServer_YYYY_MM_DD_T_HHMMSS.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return calendar
    }

    fun getServerCalendarDateFromUTCDateFormat(
        strDate: String
    ): Calendar {
        var convertedDate = Date()
        try {
            dateFormatServer_YYYY_MM_DD_T_HHMMSS.timeZone = TimeZone.getTimeZone("UTC")
            convertedDate = dateFormatServer_YYYY_MM_DD_T_HHMMSS.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        return calendar
    }

    fun getNowDate(): Date {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        return cal.time
    }

    fun getNowUTCDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        return calendar
    }

    fun getActivityDate(
        strDate: String,
    ): String {
        var convertedDate = Date()
        val fromDateFormat = dateFormatServer_YYYY_MM_DD_T_HHMMSS
        fromDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val toDateFormat = dateFormatTODAYYESTERDAY_AT_HH_MM
        toDateFormat.timeZone = TimeZone.getDefault()
        try {
            convertedDate = fromDateFormat.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        if (DateUtils.isToday(calendar.timeInMillis)) {
            return "Today" + toDateFormat.format(calendar.time)
        } else if (DateUtils.isToday(calendar.timeInMillis + DateUtils.DAY_IN_MILLIS)) {
            return "Yesterday" + toDateFormat.format(calendar.time)
        }

        val toDateFormatMMM_DD_YYYY_HH_MM = dateFormatMMM_DD_YYYY_HH_MM
        toDateFormatMMM_DD_YYYY_HH_MM.timeZone = TimeZone.getDefault()
        return toDateFormatMMM_DD_YYYY_HH_MM.format(calendar.time)
    }

    fun getPostDate(strDate: String): String {
        var convertedDate = Date()
        val fromDateFormat = dateFormatServer_YYYY_MM_DD_T_HHMMSS
        fromDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val toDateFormat = dateFormatTODAYYESTERDAY_AT_HH_MM
        toDateFormat.timeZone = TimeZone.getDefault()
        try {
            convertedDate = fromDateFormat.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = convertedDate
        if (DateUtils.isToday(calendar.timeInMillis)) {
            return "Today" + toDateFormat.format(calendar.time)
        } else if (DateUtils.isToday(calendar.timeInMillis + DateUtils.DAY_IN_MILLIS)) {
            return "Yesterday" + toDateFormat.format(calendar.time)
        }
        return dateFormatMMM_DD_YYYY_HH_MM.format(calendar.time)
    }
}