package com.frami.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.frami.R
import com.frami.data.local.pref.AppPreferencesHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.io.File


object BindingAdapters {
    private val alpha = 0.65f

//    @BindingAdapter("loadImgCrop")
//    @JvmStatic
//    fun loadImgCrop(view: AppCompatImageView, loadImgURL: String) {
//        if (loadImgURL.isEmpty()) {
//            view.setImageResource(R.drawable.ic_placeholder)
//        } else {
//            val path = FileHelper.getDirectory(view.context).absolutePath + "/" + loadImgURL
//            Glide.with(view.context)
//                .load(path)
//                .centerCrop()
//                .into(view)
//        }
//    }
//
//    @BindingAdapter("loadImgInside")
//    @JvmStatic
//    fun loadImgInside(view: AppCompatImageView, loadImgURL: String) {
//        if (loadImgURL.isEmpty()) {
//            view.setImageResource(R.drawable.ic_placeholder)
//        } else {
//            val path = FileHelper.getDirectory(view.context).absolutePath + "/" + loadImgURL
//            //Log.e("pathpathpath", "=="+path)
//            Glide.with(view.context)
//                .load(path)
//                .fitCenter()
//                .into(view)
//        }
//    }
//
//    @BindingAdapter("roundedImage37")
//    @JvmStatic
//    fun roundedImage37(imageView: AppCompatImageView, url: String?) {
//        Glide.with(imageView.context) //                .setDefaultRequestOptions(getGlideOptions(R.drawable.))
//            .load(url)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(37)))
//            .into(imageView)
//    }
//
//    @BindingAdapter("roundedImage12")
//    @JvmStatic
//    fun roundedImage12(imageView: AppCompatImageView, url: String?) {
//        Glide.with(imageView.context) //                .setDefaultRequestOptions(getGlideOptions(R.drawable.))
//            .load(url)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
//            .into(imageView)
//    }
//
//    @BindingAdapter("roundedImage16")
//    @JvmStatic
//    fun roundedImage16(imageView: AppCompatImageView, url: String?) {
//        CommonUtils.log("url>>" + url)
//        Glide.with(imageView.context) //                .setDefaultRequestOptions(getGlideOptions(R.drawable.))
//            .load(url)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
//            .into(imageView)
//    }

    //    @BindingAdapter({"app:srcCompat"})
    //    public static void srcCompat(AppCompatImageView imageView, int resource) {
    //        Glide.with(imageView.getContext())
    //                .load(resource)
    //                .into(imageView);
    //    }
    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(imageView: AppCompatImageView, imageUrl: String?) {
        CommonUtils.log("imageUrl$imageUrl")
        Glide.with(imageView.context)
            .load(if (imageUrl.isNullOrBlank()) "" else imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .placeholder(R.drawable.ic_photo_placeholder)
            .error(R.drawable.ic_photo_placeholder)
//            .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
            .into(imageView)
    }

    @BindingAdapter("loadFullWidthImage")
    @JvmStatic
    fun loadFullWidthImage(imageView: AppCompatImageView, imageUrl: String?) {
//        CommonUtils.log("imageUrl$imageUrl")
        Glide.with(imageView.context)
            .load(if (imageUrl.isNullOrBlank()) "" else imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .placeholder(R.drawable.ic_photo_placeholder)
//            .error(R.drawable.ic_placeholder)
//            .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
            .into(imageView)
    }

    @BindingAdapter("loadImageUri")
    @JvmStatic
    fun loadImageUri(imageView: AppCompatImageView, imageUri: Uri?) {
        if (imageUri == null) return
//        CommonUtils.log("imageUrl$imageUrl")
        Glide.with(imageView.context)
            .load(imageUri.path?.let { File(it) })
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .placeholder(R.drawable.ic_placeholder)
//            .error(R.drawable.ic_placeholder)
//            .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
            .into(imageView)
    }

    @BindingAdapter("loadImageFile")
    @JvmStatic
    fun loadImageFile(imageView: AppCompatImageView, imagePath: String?) {
        if (imagePath == null) return
//        CommonUtils.log("imageUrl$imageUrl")
        Glide.with(imageView.context)
            .load(imagePath)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .placeholder(R.drawable.ic_placeholder)
//            .error(R.drawable.ic_placeholder)
//            .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
            .into(imageView)
    }

    @BindingAdapter("loadCircleImage")
    @JvmStatic
    fun loadCircleImage(imageView: AppCompatImageView, imageUrl: String?) {
//        CommonUtils.log("imageUrl$imageUrl")
        Glide.with(imageView.context)
            .asBitmap()
            .load(imageUrl)
            .error(R.drawable.ic_user_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions.circleCropTransform())
//            .apply(RequestOptions().override(60, 60))
            .into(imageView)
    }

    @BindingAdapter("loadResource")
    @JvmStatic
    fun loadResource(imageView: AppCompatImageView, resourceId: Int) {
        try {
            val d: Drawable? = imageView.context.getDrawable(resourceId)
            imageView.setImageDrawable(d)
        } catch (e: Exception) {
            val d: Drawable? = imageView.context.getDrawable(R.drawable.ic_garmin)
            imageView.setImageDrawable(d)
            e.printStackTrace()
        }
    }

    @BindingAdapter("loadGif")
    @JvmStatic
    fun loadGif(imageView: AppCompatImageView, resourceId: Int) {
        try {
            Glide.with(imageView.context).asGif().load(resourceId).into(imageView);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter("loadActivityTypeImage")
    @JvmStatic
    fun loadActivityTypeImage(imageView: AppCompatImageView, imageUrl: String?) {
//        CommonUtils.log("imageUrl$imageUrl")
        Glide.with(imageView.context)
            .load(if (imageUrl.isNullOrBlank()) "" else imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_all)
//            .error(R.drawable.ic_placeholder)
//            .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
            .into(imageView)
    }

    @BindingAdapter("loadActivityTypeImage","tintColor")
    @JvmStatic
    fun loadActivityTypeImage(imageView: AppCompatImageView, imageUrl: String?,tintColor: Int) {
//        CommonUtils.log("imageUrl$imageUrl")
        Glide.with(imageView.context)
            .load(if (imageUrl.isNullOrBlank()) "" else imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_all)
//            .error(R.drawable.ic_placeholder)
//            .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
            .into(imageView)

        imageView.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
    }


//    @BindingAdapter("loadBitmap")
//    @JvmStatic
//    fun loadBitmap(imageView: ImageView, imageUrl: String?) {
//        var imageUrl = imageUrl
//        if (imageUrl.isNullOrBlank()) {
//            imageUrl = ""
//        }
//        try {
////            val decodedString: ByteArray = Base64.decode(imageUrl.replace("data:image/png;base64,",""), Base64.DEFAULT)
////            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
////            imageView.setImageBitmap(decodedByte)
//            Glide.with(imageView.context)
////                .asBitmap()
//                .load(Base64.decode(imageUrl.replace("data:image/png;base64,", ""), Base64.DEFAULT))
//                .placeholder(R.drawable.ic_placeholder)
//                .error(R.drawable.ic_placeholder)
//                .apply(RequestOptions().downsample(DownsampleStrategy.AT_MOST)) //                .fitCenter()
//                .into(imageView)
//            CommonUtils.log("imageUrl$imageUrl")
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

//    @BindingAdapter("imgURL", "borderColor", "bgColor", "txtColor", "txtName", "type")
//    @JvmStatic
//    fun borderColor(
//        view: CircleImageView,
//        imgURL: String?,
//        borderColor: Int?,
//        bgColor: Int?,
//        txtColor: Int?,
//        txtName: String?,
//        type: String?,
//    ) {
//
//        var imgURL = imgURL
//        if (imgURL == null || imgURL.trim { it <= ' ' }.isEmpty()) {
//            imgURL = ""
//        } else {
//            if (!imgURL.contains("http") && !CommonUtils.isFile(
//                    imgURL
//                )
//            ) { // It's not a complete link, something from s3 storage, append base URL
//                imgURL = imgURL
//            }
//        }
//
//        var d: Drawable? = null
//        if (!txtName.isNullOrEmpty()) {
//
//            var txtColor = txtColor
//            if (txtColor == 0) {
//                txtColor =
//                    AppPreferencesHelper(
//                        view.context,
//                        AppConstants.PREF_NAME
//                    ).getAppColor()
//            } else if (txtColor == 1) {
//                txtColor =
//                    AppPreferencesHelper(
//                        view.context,
//                        AppConstants.PREF_NAME
//                    ).getAccentColor()
//            }
//
//            var borderColor = borderColor
//            if (borderColor == 0) {
//                borderColor =
//                    AppPreferencesHelper(
//                        view.context,
//                        AppConstants.PREF_NAME
//                    ).getAppColor()
//            } else if (borderColor == 1) {
//                borderColor =
//                    AppPreferencesHelper(
//                        view.context,
//                        AppConstants.PREF_NAME
//                    ).getAccentColor()
//            }
//
//            var bgColor = bgColor
//            if (bgColor == 0) {
//                bgColor =
//                    AppPreferencesHelper(
//                        view.context,
//                        AppConstants.PREF_NAME
//                    ).getAppColor()
//            } else if (bgColor == 1) {
//                bgColor =
//                    AppPreferencesHelper(
//                        view.context,
//                        AppConstants.PREF_NAME
//                    ).getAccentColor()
//            }
//
//            view.borderColor = borderColor!!
//            val typeface: Typeface =
//                ResourcesCompat.getFont(view.context, R.font.aileron_semibold)!!
//            // default type : editProfile
//            var textSize: Int =
//                view.context.resources.getDimensionPixelSize(R.dimen.textSizeEditProfile)
//            var width: Int =
//                view.context.resources.getDimensionPixelSize(R.dimen.widthSizeEditProfile)
//            if (type == "driver_profile") {
//                textSize =
//                    view.context.resources.getDimensionPixelSize(R.dimen.textSizeViewProfile)
//                width =
//                    view.context.resources.getDimensionPixelSize(R.dimen.widthSizeViewProfile)
//            } else if (type == "home") {
//                textSize =
//                    view.context.resources.getDimensionPixelSize(R.dimen.text_size_regular)
//                width =
//                    view.context.resources.getDimensionPixelSize(R.dimen.text_size_regular)
//            }
//
//            val bitmap: Bitmap = CommonUtils.textAsBitmap(
//                typeface,
//                txtName,
//                textSize.toFloat(), width,
//                txtColor!!, bgColor!!
//            )
//            d = BitmapDrawable(view.context.resources, bitmap)
//            view.setImageDrawable(d)
//        } else {
//            d = ContextCompat.getDrawable(view.context, R.drawable.ic_placeholder)
//        }
//
//        if (!imgURL.isNullOrEmpty()) {
//            Glide.with(view.context)
//                .load(imgURL)
//                .placeholder(d)
//                .error(d)
//                .fitCenter()
//                .into(view)
//        }
//
//
//    }

    // SwitchCompat
    @BindingAdapter("switchTint")
    @JvmStatic
    fun switchTint(button: SwitchCompat, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        } else if (tintColor == 1) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        }
        val tintColorAlpha =
            ColorTransparentUtils.getColorWithAlpha(
                tintColor,
                0.5f
            )

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )

        val thumbColors = intArrayOf(
            Color.WHITE,
            tintColor
        )

        val trackColors = intArrayOf(
            Color.LTGRAY,
            tintColorAlpha
        )

        DrawableCompat.setTintList(
            DrawableCompat.wrap(button.thumbDrawable),
            ColorStateList(states, thumbColors)
        )
        DrawableCompat.setTintList(
            DrawableCompat.wrap(button.trackDrawable),
            ColorStateList(states, trackColors)
        )
    }

    // MaterialRadioButton
    @BindingAdapter("radioTint")
    @JvmStatic
    fun radioTint(button: MaterialRadioButton, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        } else if (tintColor == 1) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        }
        button.buttonTintList = ColorStateList.valueOf(tintColor)
        button.setTextColor(ColorStateList.valueOf(tintColor))
    }

    // AppCompatSpinner
    @BindingAdapter("spinnerTint")
    @JvmStatic
    fun spinnerTint(button: AppCompatSpinner, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        } else if (tintColor == 1) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        }
        val tintColorAlpha =
            ColorTransparentUtils.getColorWithAlpha(
                tintColor,
                0.5f
            )
        button.backgroundTintList = ColorStateList.valueOf(tintColorAlpha)
    }

    // MaterialCheckBox
    @BindingAdapter("checkboxTint")
    @JvmStatic
    fun checkboxTint(button: MaterialCheckBox, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        } else if (tintColor == 1) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        }
        button.buttonTintList = ColorStateList.valueOf(tintColor)
        button.setTextColor(ColorStateList.valueOf(tintColor))
    }

    // FloatingActionButton
    @BindingAdapter("backgroundTint")
    @JvmStatic
    fun backgroundTint(button: FloatingActionButton, tintColor: Int) {
        button.backgroundTintList = ColorStateList.valueOf(tintColor)
        button.imageTintList = ColorStateList.valueOf(Color.WHITE)
    }

//    @BindingAdapter("floatingIcon")
//    @JvmStatic
//    fun floatingIcon(button: FloatingActionButton, getStepVisible: Int) {
//        if (getStepVisible == 3) {
//            button.setImageResource(R.drawable.ic_done)
//        } else {
//            button.setImageResource(R.drawable.ic_arrow_next)
//        }
//    }


    @BindingAdapter("txtColorApp")
    @JvmStatic
    fun txtColorApp(view: MaterialTextView, tintColor: Int?) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    view.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        } else if (tintColor == 1) {
            tintColor =
                AppPreferencesHelper(
                    view.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        }
        view.setTextColor(ColorStateList.valueOf(tintColor!!))
    }

    @BindingAdapter("viewBG")
    @JvmStatic
    fun viewBG(view: View, tintColor: Int?) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    view.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        } else if (tintColor == 1) {
            tintColor =
                AppPreferencesHelper(
                    view.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        }
        view.setBackgroundColor(tintColor!!)
    }

    @BindingAdapter("drawableStartColor", "txtColor")
    @JvmStatic
    fun drawableStartColorTxtColor(
        view: AppCompatTextView,
        drawableStartColor: Int,
        txtColor: Int,
    ) {
        view.setTextColor(ColorStateList.valueOf(txtColor))
        val compoundDrawables: Array<Drawable> = view.compoundDrawables
        if (compoundDrawables.isNotEmpty()) {

            val drawableLeft = compoundDrawables[0].mutate()
            val drawableColor =
                ColorTransparentUtils.getColorWithAlpha(
                    drawableStartColor,
                    alpha
                )
            drawableLeft.colorFilter = PorterDuffColorFilter(
                drawableColor,
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    @BindingAdapter("cardColor")
    @JvmStatic
    fun cardBackgroundColor(view: MaterialCardView, cardColor: Int) {
        if (cardColor == 0) {
            val finalTintColor: Int =
                AppPreferencesHelper(
                    view.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
            view.setCardBackgroundColor(ColorStateList.valueOf(finalTintColor))
        } else if (cardColor == 1) {
            val finalTintColor: Int =
                AppPreferencesHelper(
                    view.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
            view.setCardBackgroundColor(ColorStateList.valueOf(finalTintColor))
        } else {
            view.setCardBackgroundColor(ColorStateList.valueOf(cardColor))
        }

    }


    @BindingAdapter("imageTint")
    @JvmStatic
    fun imageTint(view: AppCompatImageView, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor = AppPreferencesHelper(
                view.context,
                AppConstants.PREF_NAME
            ).getAppColor()
        } else if (tintColor == 1) {
            tintColor = AppPreferencesHelper(
                view.context,
                AppConstants.PREF_NAME
            ).getAccentColor()
        }
        view.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
    }

    @BindingAdapter("buttonColor")
    @JvmStatic
    fun buttonColor(button: MaterialButton, tintColor: Int?) {
        var tintColor = tintColor
        if (tintColor == null) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        }
        button.backgroundTintList = ColorStateList.valueOf(tintColor)

        val textColor =
            AppPreferencesHelper(
                button.context,
                AppConstants.PREF_NAME
            ).getAppColor()
        button.setTextColor(ColorStateList.valueOf(textColor))
    }

    @BindingAdapter("rippleColor")
    @JvmStatic
    fun rippleColor(button: MaterialButton, tintColor: Int?) {
        var tintColor = tintColor
        if (tintColor == null) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAccentColor()
        }
        button.rippleColor = ColorStateList.valueOf(tintColor)

    }


    // Material Edit text
    @BindingAdapter("editText")
    @JvmStatic
    fun editText(button: TextInputLayout, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        }
        tintColor = ColorTransparentUtils.getColorWithAlpha(
            tintColor,
            alpha
        )
        button.setStartIconTintList(ColorStateList.valueOf(tintColor))
        button.setEndIconTintList(ColorStateList.valueOf(tintColor))
        button.hintTextColor = ColorStateList.valueOf(tintColor)
        button.boxStrokeColor = tintColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            button.editText!!.setTextColor(
                ContextCompat.getColor(
                    button.context,
                    R.color.black
                )
            )
        } else {
            button.editText!!.setTextColor(button.context.resources.getColor(R.color.black))
        }

    }

    // Material Edit text
    @BindingAdapter("editTextEndIcon")
    @JvmStatic
    fun editTextEndIcon(button: TextInputLayout, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(
                    button.context,
                    AppConstants.PREF_NAME
                ).getAppColor()
        }
        tintColor = ColorTransparentUtils.getColorWithAlpha(
            tintColor,
            alpha
        )
        button.setEndIconTintList(ColorStateList.valueOf(tintColor))
        button.hintTextColor = ColorStateList.valueOf(tintColor)
        button.boxStrokeColor = tintColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            button.editText!!.setTextColor(
                ContextCompat.getColor(
                    button.context,
                    R.color.black
                )
            )
        } else {
            button.editText!!.setTextColor(button.context.resources.getColor(R.color.black))
        }

    }

    @BindingAdapter("handleEnable")
    @JvmStatic
    fun enable(mView: View, isEnable: Boolean) {
        mView.isEnabled = isEnable
        mView.alpha = if (isEnable) 1f else 0.3f
    }

    @BindingAdapter(value = ["roundedImage", "roundedRadius"], requireAll = false)
    @JvmStatic
    fun roundedImage(imageView: AppCompatImageView, url: String?, roundedRadius: Float?) {
        CommonUtils.log("Radius>> " + roundedRadius)
        Glide.with(imageView.context)
//            .setDefaultRequestOptions(getGlideOptions(R.drawable.ic_user)) //TODO Change Please holder
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(roundedRadius?.let { Math.round(it) }
                ?: 8)))
            .into(imageView)
    }
//
//    @BindingAdapter("roundedImage8")
//    @JvmStatic
//    fun roundedImage8(imageView: AppCompatImageView, url: String?) {
////        if (TextUtils.isEmpty(url)) {
////            return
////        }
//        Glide.with(imageView.context)
//            .setDefaultRequestOptions(getGlideOptions(R.drawable.ic_placeholder))
//            .load(url)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
//            .into(imageView)
//    }
//
//    @BindingAdapter("roundedImage40")
//    @JvmStatic
//    fun roundedImage40(imageView: AppCompatImageView, url: String?) {
////        if (TextUtils.isEmpty(url)) {
////            return
////        }
//        Glide.with(imageView.context)
//            .setDefaultRequestOptions(getGlideOptions(R.drawable.ic_placeholder))
//            .load(url)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(160)))
//            .into(imageView)
//    }
//
//    @BindingAdapter("roundedImage20")
//    @JvmStatic
//    fun roundedImage20(imageView: AppCompatImageView, url: Int) {
////        if (TextUtils.isEmpty(url)) {
////            return
////        }
//        Glide.with(imageView.context)
//            .setDefaultRequestOptions(getGlideOptions(R.drawable.ic_placeholder))
//            .load(url)
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(160)))
//            .into(imageView)
//    }

    @BindingAdapter("setThemeTextColor")
    @JvmStatic
    fun setThemeTextColor(textView: TextView, tintColor: Int) {
        var tintColor = tintColor
        if (tintColor == 0) {
            tintColor =
                AppPreferencesHelper(textView.context, AppConstants.PREF_NAME).getAccentColor()
        }
        val states = arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        )
        val colors = intArrayOf(
            textView.context.resources.getColor(R.color.themeColor),
            textView.context.resources.getColor(R.color.themeColor),
            tintColor
        )
        val colorStateList = ColorStateList(states, colors)
        textView.setTextColor(colorStateList)
    }

    @BindingAdapter("drawablePadding")
    @JvmStatic
    fun setDrawablePadding(view: AppCompatTextView, dimen: Float) {
        view.compoundDrawablePadding = dimen.toInt()
    }
}