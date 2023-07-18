package com.frami.utils.widget.toolbar

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.frami.R


class ERToolbarWhiteIconColor(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {

    init {
        val tintColor: Int = ContextCompat.getColor(context, R.color.white)
        setTitleTextColor(tintColor)

        val upArrow = navigationIcon
        setSubtitleTextColor(ContextCompat.getColor(context, R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            upArrow!!.colorFilter = BlendModeColorFilter(tintColor, BlendMode.SRC_ATOP)
        } else {
            upArrow!!.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
        }
        navigationIcon = upArrow
    }

    override fun inflateMenu(resId: Int) {
        super.inflateMenu(resId)

        val menu = menu
        val tintColor =
            ContextCompat.getColor(context, R.color.white)
        MenuTintUtils.tintAllIcons(
            menu,
            tintColor,
            context
        )
    }
}