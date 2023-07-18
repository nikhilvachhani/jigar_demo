package com.frami.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.frami.R;
import com.frami.databinding.HtmlTextViewLayoutBinding;
import com.frami.utils.BindingAdapters;


//public class HTMLTextView2 extends LinearLayout implements View.OnClickListener {
//    private HtmlTextViewLayoutBinding binding;
//    private int trimLines = 4;
//
//    public HTMLTextView2(Context context) {
//        super(context);
//        this.init();
//    }
//
//    public HTMLTextView2(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView);
//        this.trimLines = typedArray.getInt(R.styleable.ReadMoreTextView_trimLines, 4);
//        this.init();
//    }
//
//    public HTMLTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView);
//        this.trimLines = typedArray.getInt(R.styleable.ReadMoreTextView_trimLines, 4);
//        this.init();
//    }
//
//    private void init() {
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        binding = DataBindingUtil.inflate(inflater, R.layout.html_text_view_layout, this, true);
////        binding.txtDescription.setTrimLines(trimLines);
//        // Set on click listener
//        binding.btnReadMore.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (binding.txtDescription.isCollapsed()) {
//            binding.btnReadMore.setText(view.getContext().getString(R.string.read_more));
//        } else {
//            binding.btnReadMore.setText(view.getContext().getString(R.string.read_less));
//        }
//    }
//
//    public void setTextColor(int textColor) {
//        BindingAdapters.setThemeTextColor(binding.btnReadMore, textColor);
//        binding.executePendingBindings();
//    }
//
//    public void setText(String text) {
//        if (binding.txtDescription != null) {
//            text = text == null ? "" : text;
////            setText(new SpannableString(Html.fromHtml(text)));
//            setText(new SpannableString(text));
//        }
//    }
//
//    private void updateButtonVisibility() {
//        // Hide view if text is empty
//        if (TextUtils.isEmpty(binding.txtDescription.getText())) {
//            setVisibility(GONE);
//            return;
//        } else {
//            setVisibility(VISIBLE);
//        }
//
//        if (binding.txtDescription.getMaxLines() >= 4) {
//            binding.btnReadMore.setVisibility(VISIBLE);
//        } else {
//            binding.btnReadMore.setVisibility(GONE);
//        }
//    }
//
//    public void setText(SpannableString spannableString) {
//        if (binding.txtDescription != null && spannableString != null) {
//            binding.txtDescription.setText(spannableString);
//            updateButtonVisibility();
//        }
//    }
//}