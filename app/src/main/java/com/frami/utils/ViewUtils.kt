package com.frami.utils

import android.view.View

object ViewUtils {
    fun disableView(view: View, alpha: Float) {
        view.isEnabled = false
        view.alpha = alpha
    }

    fun enableView(view: View) {
        view.isEnabled = true
        view.alpha = 1.0f
    }

//    fun loadImage(imageView: ImageView, imageUrl: String?) {
//        var imageUrl = imageUrl
//        if (imageUrl.isNullOrBlank()) {
//            imageUrl = ""
//        }
//        CommonUtils.log("imageUrl$imageUrl")
//        Glide.with(imageView.context)
//            .load(imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .placeholder(R.drawable.ic_placeholder)
//            .error(R.drawable.ic_placeholder)
//            .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
//            .into(imageView)
//    }
}