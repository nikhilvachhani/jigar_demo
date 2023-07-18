package com.frami.utils

import android.graphics.Color
import com.frami.R

object ColorTransparentUtils {
    // This default color int
    val defaultColor: Int = R.color.colorPrimaryDark

    fun getColorWithAlpha(color: Int, ratio: Float): Int {
        return Color.argb(
            Math.round(Color.alpha(color) * ratio),
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    /**
     * Convert color code into transparent color code
     * @param colorCode color code
     * @param transCode transparent number
     * @return transparent color code
     */
    fun convertIntoColor(
        colorCode: Int,
        transCode: Int
    ): String? { // convert color code into hexa string and remove starting 2 digit
        val color =
            Integer.toHexString(colorCode).toUpperCase().substring(2)
        return if (!color.isEmpty() && transCode > 100) {
            if (color.trim { it <= ' ' }.length == 6) {
                "#" + convert(transCode).toString() + color
            } else {
                convert(transCode).toString() + color
            }
        } else "#" + Integer.toHexString(defaultColor).toUpperCase().substring(2)
        // if color is empty or any other problem occur then we return deafult color;
    }


    /**
     * This method convert numver into hexa number or we can say transparent code
     * @param trans number of transparency you want
     * @return it return hex decimal number or transparency code
     */
    fun convert(trans: Int): String? {
        val hexString =
            Integer.toHexString(Math.round(255 * trans / 100.toFloat()))
        return (if (hexString.length < 2) "0" else "") + hexString
    }
}