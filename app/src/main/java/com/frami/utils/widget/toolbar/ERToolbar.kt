package com.frami.utils.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import com.frami.data.local.pref.AppPreferencesHelper
import com.frami.utils.AppConstants
import com.frami.utils.widget.toolbar.MenuTintUtils.tintAllIcons

class ERToolbar : Toolbar {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
    }

    override fun inflateMenu(@MenuRes resId: Int) {
        super.inflateMenu(resId)
        val menu = menu
        val tintColor: Int = AppPreferencesHelper(context, AppConstants.PREF_NAME).getAppColor()
        tintAllIcons(menu, tintColor, context)
    }
}