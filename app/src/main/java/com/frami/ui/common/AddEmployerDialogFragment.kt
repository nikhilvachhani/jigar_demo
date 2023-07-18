package com.frami.ui.common

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.frami.R

class AddEmployerDialogFragment(
    private var dialogListener: DialogListener,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_employer, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
    }

    private fun setupView(view: View) {
//        view.tvGuide.text = arguments?.getString(KEY_TITLE)
        setInfoText1(view)
        setInfoText2(view)
    }

    private fun setupClickListeners(view: View) {
        val ivClose = view.findViewById<AppCompatImageView>(R.id.ivClose)
        ivClose.setOnClickListener { this.dismiss() }
    }

    private fun setInfoText1(view: View) {
        val tvInfoText1 = view.findViewById<AppCompatTextView>(R.id.tvInfoText1)
        val spannableString = SpannableString(getString(R.string.add_employer_info_1))
        val tfRegular = ResourcesCompat.getFont(requireContext(), R.font.sf_pro_regular)
        val tfBold = ResourcesCompat.getFont(requireContext(), R.font.sf_pro_bold)
        val contactInfo: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                dismiss()
                dialogListener.onContactInfoNavigation()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        try {
            spannableString.setSpan(
                tfRegular,
                0,
                80,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                contactInfo,
                80,
                84,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorHeaderText
                    )
                ), 0, 80, 0
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.themeColor
                    )
                ), 80, 84, 0
            )
            spannableString.setSpan(
                tfBold,
                80,
                84,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tvInfoText1.movementMethod = LinkMovementMethod.getInstance()
        tvInfoText1.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun setInfoText2(view: View) {
        val tvInfoText2 = view.findViewById<AppCompatTextView>(R.id.tvInfoText2)
        val spannableString = SpannableString(getString(R.string.add_employer_info_2))
        val tfRegular = ResourcesCompat.getFont(requireContext(), R.font.sf_pro_regular)
        val tfBold = ResourcesCompat.getFont(requireContext(), R.font.sf_pro_bold)
        val communityInfo: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                dismiss()
                dialogListener.onCommunityListNavigation()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        try {
            spannableString.setSpan(
                tfRegular,
                0,
                61,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                communityInfo,
                61,
                65,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorHeaderText
                    )
                ), 0, 61, 0
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.themeColor
                    )
                ), 61, 65, 0
            )
            spannableString.setSpan(
                tfBold,
                61,
                65,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tvInfoText2.movementMethod = LinkMovementMethod.getInstance()
        tvInfoText2.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    interface DialogListener {
        fun onContactInfoNavigation()
        fun onCommunityListNavigation()
    }
}