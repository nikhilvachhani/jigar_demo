package com.frami.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalLimitInputFilter(maxDecimalPlaces: Int) : InputFilter {
	private val pattern: Pattern = Pattern.compile("[0-9]+(\\.[0-9]{0,$maxDecimalPlaces})?")

	override fun filter(
		source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int
	): CharSequence? {
		val builder = StringBuilder(dest.toString())
		builder.replace(dstart, dend, source.toString())
		val matcher = pattern.matcher(builder.toString())
		return if (matcher.matches()) null else ""
	}
}
