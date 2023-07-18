package com.frami.utils.extensions

import android.content.Context
import android.net.Uri
import android.text.Html
import android.text.InputType
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.frami.R
import com.frami.utils.listdecorators.EqualSpacingItemDecoration
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

/*TODO: Remove unused methods*/

fun ImageView.loadImage(url: String?) {
    Glide
        .with(context)
        .load(url)
        .error(R.drawable.ic_user_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadImageUri(imageUri: Uri?) {
    Glide
        .with(context)
        .load(imageUri?.path?.let { File(it) })
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadImageWithRoundCorners(url: Uri?, cornerRadius: Float = 0f) {
    Glide
        .with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(cornerRadius.toInt())))
        .into(this)

}

fun ImageView.loadCircleImage(url: Uri?) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .error(R.drawable.ic_user_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .apply(RequestOptions.circleCropTransform())
//            .apply(RequestOptions().override(60, 60))
        .into(this)

}

//fun ImageView.loadImageWithRoundCorners(reference: StorageReference, cornerRadius: Float = 0f) {
//  Glide
//    .with(context)
//    .load(reference)
//    .transition(DrawableTransitionOptions.withCrossFade())
//    .apply(RequestOptions().apply {
//      transforms(CenterCrop(), RoundedCorners(cornerRadius.toInt()))
//    })
//
//    .into(this)
//
//}

//fun ImageView.loadImage(reference: StorageReference) {
//  Glide
//    .with(context)
//    .load(reference)
//    .transition(DrawableTransitionOptions.withCrossFade())
//    .into(this)
//}

fun ImageView.loadImage(@DrawableRes resId: Int) {
//    Glide.with(context).load(resId).into(this)
}

fun View.disable(alpha: Float = 0.3f) {
    this.isEnabled = false
    this.alpha = alpha
}

fun View.enable() {
    this.isEnabled = true
    this.alpha = 1f
}

fun View.invisible() {
    this.visibility = INVISIBLE
}

fun View.visible() {
    this.visibility = VISIBLE
}

fun View.hide() {
    this.visibility = GONE
}

fun ViewGroup.inflate(@LayoutRes resourceId: Int): View {
    return LayoutInflater.from(this.context).inflate(resourceId, this, false)
}

fun Context.inflate(@LayoutRes resourceId: Int): View {
    return LayoutInflater.from(this).inflate(resourceId, null)
}

fun TextInputLayout.clearError() {
    this.error = null
    this.isErrorEnabled = false
}

fun TextInputLayout.showError(@StringRes stringId: Int) {
    this.isErrorEnabled = true
    this.error = resources.getString(stringId)
}

fun EditText.getTextString(): String {
    return this.text.toString().trim()
}

fun EditText.disableInput() {
    this.isFocusable = false
    this.isClickable = true
    this.inputType = InputType.TYPE_NULL
}

fun EditText.enableInput(inputType: Int = InputType.TYPE_CLASS_TEXT) {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.isClickable = true
    this.inputType = inputType
}

fun EditText.getIntValue(defaultValue: Int): Int {
    val string = this.text.toString().trim()

    return try {
        string.toInt()
    } catch (e: NumberFormatException) {
        defaultValue
    }
}

fun EditText.getLongValue(defaultValue: Long): Long {
    val string = this.text.toString().trim()

    return try {
        string.toLong()
    } catch (e: NumberFormatException) {
        defaultValue
    }
}

fun TextView.getDoubleValue(defaultValue: Double): Double {
    val string = this.text.toString().trim()

    return try {
        string.toDouble()
    } catch (e: NumberFormatException) {
        defaultValue
    }
}

fun EditText.getDoubleValue(defaultValue: Double): Double {
    val string = this.text.toString().trim().replace(",", ".", true)

    return try {
        string.toDouble()
    } catch (e: NumberFormatException) {
        defaultValue
    }
}

fun TextView.applyRowHeaderStyle() {
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
//    this.typeface = ResourcesCompat.getFont(context!!, R.font.aileron_semibold)
    this.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
}

fun TextView.applyRowFooterStyle() {
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
//    this.typeface = ResourcesCompat.getFont(context!!, R.font.aileron_semibold)
//    this.setTextColor(ContextCompat.getColor(context!!, R.color.colorHeaderText))
}

fun TextView.displayHtmlText(htmlString: String) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
    } else {
        this.text = Html.fromHtml(htmlString)
    }
}

fun RecyclerView.addItemSpacing(
    @DimenRes spacing: Int = R.dimen.list_item_spacing,
    orientation: Int
) {
    this.addItemDecoration(
        EqualSpacingItemDecoration(
            resources.getDimension(spacing).toInt(),
            orientation
        )
    )
}

fun RecyclerView.addDividers(orientation: Int) {
//    this.addItemDecoration(DividerItemDecoration(context, orientation).also {
//      ContextCompat.getDrawable(context, R.drawable.bg_item_divider)?.let { drawable ->
//        it.setDrawable(drawable)
//      }
//    })
}

fun roundOffDecimal(number: Double): Double {
    val df = DecimalFormat("##.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}

fun format2DecimalPoints(number: Double): Double {
    val df = DecimalFormat("##.##")
    return df.format(number).toDouble()
}