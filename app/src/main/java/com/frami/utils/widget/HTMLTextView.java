package com.frami.utils.widget;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.frami.R;
import com.frami.databinding.HtmlTextViewLayoutBinding;
import com.frami.utils.BindingAdapters;


public class HTMLTextView extends LinearLayout implements View.OnClickListener {
    private HtmlTextViewLayoutBinding binding;

    public HTMLTextView(Context context) {
        super(context);
        this.init();
    }

    public HTMLTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public HTMLTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.html_text_view_layout, this, true);

        // Set on click listener
        binding.btnReadMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        if (binding.txtDescription.toggle()) {
//            binding.btnReadMore.setText(view.getContext().getString(R.string.read_more));
//        } else {
//            binding.btnReadMore.setText(view.getContext().getString(R.string.read_less));
//        }
    }

    public void setTextColor(int textColor) {
        BindingAdapters.setThemeTextColor(binding.btnReadMore, textColor);
        binding.executePendingBindings();
    }

    public void setText(String text) {
        if (binding.txtDescription != null) {
            text = text == null ? "" : text;
            setText(new SpannableString(Html.fromHtml(text)));
        }
    }

    private void updateButtonVisibility() {
        // Hide view if text is empty
        if (binding.txtDescription.isTextEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

//        if (binding.txtDescription.isAdditionalText()) {
//            binding.btnReadMore.setVisibility(VISIBLE);
//        } else {
//            binding.btnReadMore.setVisibility(GONE);
//        }
    }

    public void setText(SpannableString spannableString) {
        if (binding.txtDescription != null && spannableString != null) {
            binding.txtDescription.setText(spannableString);
            updateButtonVisibility();
        }
    }
}