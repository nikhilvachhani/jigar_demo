package com.frami.ui.common.videopicker

import android.graphics.Bitmap

data class MediaGalleryModel(val path: String, val name: String, val duration: Long,val durationFormated: String,val fileSize : Long,val type : String,val album : String? = null, val albumBitmap : Bitmap? = null)