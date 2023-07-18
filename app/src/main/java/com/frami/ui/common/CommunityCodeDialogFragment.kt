package com.frami.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.frami.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class CommunityCodeDialogFragment(
    private var dialogListener: DialogListener,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_community_code, container, false)
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
        clickListeners(view)
    }

    private fun setupView(view: View) {
//        view.tvGuide.text = arguments?.getString(KEY_TITLE)
    }

    private fun clickListeners(view: View) {
        val etCode = view.findViewById<TextInputEditText>(R.id.etCode)
        val btnConnect = view.findViewById<MaterialButton>(R.id.btnConnect)
        btnConnect.setOnClickListener {
            this.dismiss();
            val code = etCode.text.toString().trim()
            dialogListener.onCommunityCodeConnect(code)
        }
    }


    interface DialogListener {
        fun onCommunityCodeConnect(code: String)
    }
}