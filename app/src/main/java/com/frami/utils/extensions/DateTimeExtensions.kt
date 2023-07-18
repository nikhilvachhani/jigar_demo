package com.frami.utils.extensions

import android.text.format.DateUtils
import com.frami.utils.AppConstants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Formats the date to a string according to the DateFormat passed.
 * @param dateFormat Valid date format(preferably defined in t@AppConstants.DateFormat) in which you want to format the date, e.g. dd-MM-yyyy.
 * @return Formatted date string according to the DateFormat passed.
* */
fun Date.formatText(@AppConstants.DateFormat dateFormat: String): String {
  return SimpleDateFormat(dateFormat, Locale.getDefault()).format(this)
}

/**
 * Converts the timestamp value to a date object.
 * @return Date instance created from provided timestamp value.
 **/
fun Long.getDate(): Date {

  Calendar.getInstance().let {
    if (this > 0) {
      it.timeInMillis = this
    }
    return it.time
  }

}

fun String.formatText(
  @AppConstants.DateFormat srcDateFormat: String, @AppConstants.DateFormat requiredInFormat: String,
  defaultValue: String = ""
): String {

  if (this.isEmpty()) return defaultValue

  val timestamp = try {
    SimpleDateFormat(srcDateFormat, Locale.US).parse(this)
  } catch (exception: ParseException) {
    null
  }

  return if (timestamp == null) defaultValue
  else {
    SimpleDateFormat(requiredInFormat, Locale.US).format(timestamp)
  }
}

fun Long.formatTimeStamp(@AppConstants.DateFormat timeFormat: String): String {

  if (this == 0L)
    return ""

  val timestamp = if (this.toString().length <= 10) this * 1000 else this
  return try {
    SimpleDateFormat(timeFormat, Locale.US).format(Date(timestamp))
  } catch (e: Exception) {
    ""
  }
}

fun Long.formatAsTimeAgo(): String {

  if (this == 0L)
    return ""

  val timestamp = if (this.toString().length <= 10) this * 1000 else this

  return DateUtils.getRelativeTimeSpanString(
    timestamp,
    Calendar.getInstance().timeInMillis,
    DateUtils.SECOND_IN_MILLIS
  ).toString()
}

fun Long.formatTicketListHeader(): String {

  val timestamp = if (this.toString().length <= 10) this * 1000 else this

  if (DateUtils.isToday(timestamp)) {
    return "Today"
  }

  if (DateUtils.isToday(timestamp + DateUtils.DAY_IN_MILLIS)) {
    return "Yesterday"
  }

  if (timestamp < Calendar.getInstance().timeInMillis)
    return "Previous"

  return ""
}

/**
 * Compares two dates and return true if both the dates are same else false
 * @param afterDate the date to compare with source date.
 * @return true if the both the dates have same exact date else false
 * */
fun Date.isSameDay(afterDate: Date): Boolean {
  return try {
    this.formatText(AppConstants.DATE_FORMAT_DD_MM_YY)
      .equals(afterDate.formatText(AppConstants.DATE_FORMAT_DD_MM_YY), true)
  } catch (e: Exception) {
    false
  }
}

fun String.getMonthValue(): Int {
  val months = arrayOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  )
  val monthsShort =
    arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

  months.indexOf(this).let {
    if (it < 0) {
      monthsShort.indexOf(this).let { index ->
        if (index >= 0)
          return index
      }
    } else return it
  }

  return 0
}

fun Long.trimMillisFromTimeStamp(): Long {
  if (this.toString().length > 10)
    return this / 1000

  return this
}

fun Long.addMillisToTimestamp(): Long {
  if ((this/10000000000000) <= 0)
    return this * 1000

  return this
}

/**
 * Calculates age as a difference between the current date and the date passed.
 * @return Calculated age value as a string else returns empty string if age is smaller or equals to 0.
 **/
fun Date.calculateAge(): Int {
  val dob = Calendar.getInstance().let {
    it.time = this
    it
  }

  val today = Calendar.getInstance()

  var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

  if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) age--

  return Math.max(0, age)
}

