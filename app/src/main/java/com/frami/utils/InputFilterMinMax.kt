package com.frami.utils

import android.text.InputFilter
import android.text.Spanned

class InputFilterMinMax(private val min: Number, private val max: Number) : InputFilter {
	init {
		if (min.toDouble() > max.toDouble()) {
			throw IllegalArgumentException("min ($min) must not be higher than max ($max)")
		}
	}
	override fun filter(
		source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int
	): CharSequence? {
		val builder = StringBuilder(dest.toString())
		val result = builder.replace(dstart, dend, source.toString()).toString()
		if (result.isEmpty()) return null
		if (result.matches("^\\.\\d*".toRegex())) {
			if (source == ".") {
				return "0."
			} else if (source.isEmpty()) {
				return "0"
			}
		}
		if (result.length - result.replace(".", "").length > 1) {
			return dest.toString().substring(dstart, dend)
		}
		return try {
			if (isInRange(result)) null else ""
		} catch (e: NumberFormatException) {
			e.printStackTrace()
			""
		}
	}

	private fun isInRange(input: String): Boolean {
		return if (min is Int && max is Int) {
			input.toInt() in min..max
		} else if (min is Float && max is Float) {
			input.toFloat() in min..max
		} else throw NumberFormatException(
			"Unsupported number type. min: ${min::class.simpleName}, max: ${min::class.simpleName}"
		)
	}
}
