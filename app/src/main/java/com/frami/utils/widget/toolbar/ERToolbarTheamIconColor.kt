package com.frami.utils.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.frami.R
import com.frami.data.local.pref.AppPreferencesHelper
import com.frami.utils.AppConstants


class ERToolbarTheamIconColor(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {

    init {
        val tintColor: Int = AppPreferencesHelper(
            context,
            AppConstants.PREF_NAME
        ).getAppColor()
        setBackgroundColor(tintColor)

        val upArrow = navigationIcon
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            upArrow!!.colorFilter = BlendModeColorFilter(ContextCompat.getColor(context, R.color.white), BlendMode.SRC_ATOP)
//        } else {
//            upArrow!!.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_ATOP)
//        }
        setSubtitleTextColor(ContextCompat.getColor(context, R.color.white))
        setTitleTextColor(ContextCompat.getColor(context, R.color.white))
        navigationIcon = upArrow
    }

    override fun inflateMenu(resId: Int) {
        super.inflateMenu(resId)

        val menu = menu
        val tintColor = ContextCompat.getColor(context, R.color.white)
        MenuTintUtils.tintAllIcons(
            menu,
            tintColor,
            context
        )
    }
}