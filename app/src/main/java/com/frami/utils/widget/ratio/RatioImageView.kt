package com.frami.utils.widget.ratio


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.frami.R


/**
 * @author Jigar Moradiya
 */
class RatioImageView : AppCompatImageView,
    RatioBase {
    override var verticalRatio = 1f
        private set
    override var horizontalRatio = 1f
        private set
    override var fixedAttribute =
        FixedAttribute.WIDTH
        private set

    constructor(context: Context?) : super(context!!) {}
    constructor(
        context: Context?,
        attrs: AttributeSet
    ) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.RatioRelativeLayout
        )
        fixedAttribute =
            FixedAttribute.fromId(
                typedArray.getInt(
                    R.styleable.RatioRelativeLayout_fixed_attribute,
                    0
                )
            )
        horizontalRatio = typedArray.getFloat(R.styleable.RatioRelativeLayout_horizontal_ratio, 1f)
        verticalRatio = typedArray.getFloat(R.styleable.RatioRelativeLayout_vertical_ratio, 1f)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val originalHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (fixedAttribute === FixedAttribute.WIDTH) {
            val calculatedHeight = (originalWidth * (verticalRatio / horizontalRatio)).toInt()
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(
                    calculatedHeight,
                    MeasureSpec.EXACTLY
                )
            )
        } else {
            val calculatedWidth = (originalHeight * (horizontalRatio / verticalRatio)).toInt()
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(
                    calculatedWidth,
                    MeasureSpec.EXACTLY
                ), heightMeasureSpec
            )
        }
    }

    override fun setRatio(horizontalRatio: Float, verticalRatio: Float) {
        this.setRatio(fixedAttribute, horizontalRatio, verticalRatio)
    }

    override fun setRatio(
        fixedAttribute: FixedAttribute,
        horizontalRatio: Float,
        verticalRatio: Float
    ) {
        this.fixedAttribute = fixedAttribute
        this.horizontalRatio = horizontalRatio
        this.verticalRatio = verticalRatio
        this.invalidate()
        requestLayout()
    }

}