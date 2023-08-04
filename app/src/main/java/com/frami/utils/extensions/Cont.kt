package com.frami.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.frami.ui.common.fullscreen.FullScreenImageActivity
import com.frami.utils.AppConstants
import java.util.ArrayList

fun Context.fullScreenImage(list: List<String>) {
    val intent = Intent(this, FullScreenImageActivity::class.java)
    val bundle = Bundle()
    bundle.putStringArrayList(AppConstants.EXTRAS.IMAGE_URL_LIST,list as ArrayList<String>)
    intent.putExtras(bundle)
    startActivity(intent)
}
fun Context.isPermissionGrant(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

