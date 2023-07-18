package com.frami.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

public class PhoneTextFormatter implements TextWatcher {

    private final String TAG = this.getClass().getSimpleName();

    private AppCompatEditText mEditText;

    private String mPattern;

    public PhoneTextFormatter(AppCompatEditText editText, String pattern) {
        mEditText = editText;
        mPattern = pattern;
        //set max length of string
        int maxLength = pattern.length();
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        StringBuilder phone = new StringBuilder(s);

        Log.d(TAG, "join");

        if (count > 0 && !isValid(phone.toString())) {
            for (int i = 0; i < phone.length(); i++) {
                Log.d(TAG, String.format("%s", phone));
                char c = mPattern.charAt(i);

                if ((c != '#') && (c != phone.charAt(i))) {
                    phone.insert(i, c);
                }
            }

            mEditText.setText(phone);
            mEditText.setSelection(mEditText.getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean isValid(String phone) {
        for (int i = 0; i < phone.length(); i++) {
            char c = mPattern.charAt(i);

            if (c == '#') continue;

            if (c != phone.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}