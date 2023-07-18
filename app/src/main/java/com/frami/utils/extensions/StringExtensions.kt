package com.frami.utils.extensions

fun String?.formatText(defaultValue: String = "N/A"): String {
  return if (this.isNullOrEmpty()) defaultValue else this
}

fun String.getIntValue(defaultValue: Int = 0): Int {
  return try {
    this.toInt()
  } catch (e: Exception) {
    defaultValue
  }
}