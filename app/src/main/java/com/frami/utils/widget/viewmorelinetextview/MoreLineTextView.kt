package com.frami.utils.widget.viewmorelinetextview

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.databinding.HtmlTextViewLayoutBinding
import com.frami.utils.BindingAdapters.setThemeTextColor

class MoreLineTextView : LinearLayout, View.OnClickListener {
    private var binding: HtmlTextViewLayoutBinding? = null
    //    private var readMoreMaxLine = 4

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)
//        readMoreMaxLine = typedArray.getInt(R.styleable.ReadMoreTextViewByLine_readMoreMaxLine, 4)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)
//        readMoreMaxLine = typedArray.getInt(R.styleable.ReadMoreTextViewByLine_readMoreMaxLine, 4)
        init()
    }

    private fun init() {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.html_text_view_layout, this, true)
        // Set on click listener
        binding!!.btnReadMore.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (binding!!.txtDescription.isExpanded) {
            binding!!.txtDescription.state = ReadMoreTextView.State.COLLAPSED
            binding!!.btnReadMore.text = view.context.getString(R.string.read_more)
        } else {
            binding!!.txtDescription.state = ReadMoreTextView.State.EXPANDED
            binding!!.btnReadMore.text = view.context.getString(R.string.read_less)
        }
    }

    fun setTextColor(textColor: Int) {
        setThemeTextColor(binding!!.btnReadMore, textColor)
        binding!!.executePendingBindings()
    }

    fun setText(text: String?) {
        var text = text
        text = text ?: ""
        //            setText(new SpannableString(Html.fromHtml(text)));
        setText(SpannableString(text))
    }

    private fun updateButtonVisibility() {
        binding!!.txtDescription.post(Runnable {
            val lineCount: Int = binding!!.txtDescription.lineCount
            // Use lineCount here
            if (lineCount > 4) {
                binding!!.btnReadMore.visibility = VISIBLE
            } else {
                binding!!.btnReadMore.visibility = GONE
            }
        })
    }

    fun setText(spannableString: SpannableString?) {
        if (spannableString != null) {
            binding!!.txtDescription.text = spannableString
            updateButtonVisibility()
        }
    }

}