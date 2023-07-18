package com.frami.utils.widget.toolbar

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.frami.R
import com.frami.utils.ColorTransparentUtils
import java.lang.reflect.Field


object MenuTintUtils {
    fun getStatusBarHeight(context: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = field[obj].toString().toInt()
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }

    fun tintAllIcons(menu: Menu, color: Int, context: Context) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)

            //for applying to subMenu ...
            if (item.subMenu != null && item.subMenu!!.size() > 0) {
                val subMenu: SubMenu = item.subMenu!!
                val tintColorAlpha = ColorTransparentUtils.getColorWithAlpha(color, 0.5f)
                for (j in 0 until subMenu.size()) {
                    val subMenuItem = subMenu.getItem(j)
                    tintMenuItemIcon(
                        color,
                        subMenuItem
                    )
                    applyFontToMenuItem(
                        subMenuItem,
                        tintColorAlpha,
                        context
                    )
                }
            } else {
                tintMenuItemIcon(
                    color,
                    item
                )
                applyFontToMenuItem(
                    item,
                    color,
                    context
                )
            }

        }


    }

    private fun tintMenuItemIcon(color: Int, item: MenuItem) {
        val drawable = item.icon
        if (drawable != null) {
            val wrapped = DrawableCompat.wrap(drawable)
            drawable.mutate()
            DrawableCompat.setTint(wrapped, color)
            item.icon = drawable
        }
    }

    private fun applyFontToMenuItem(mi: MenuItem, color: Int, context: Context) {
        val mNewTitle = SpannableString(mi.title)
        val typeface: Typeface = ResourcesCompat.getFont(context, R.font.sf_pro_bold)!!

        mNewTitle.setSpan(
            CustomTypefaceSpan("", typeface),
            0,
            mNewTitle.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        ) // set font family
//        mNewTitle.setSpan(ForegroundColorSpan(color), 0, mNewTitle.length, 0) // set font color
        val textSize: Int = context.resources.getDimensionPixelSize(R.dimen.text_size_regular)
        mNewTitle.setSpan(AbsoluteSizeSpan(textSize), 0, mNewTitle.length, 0) // set font size
        mi.title = mNewTitle
    }
}