package com.frami.ui.settings.wearable.success

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.wearable.WearableData
import com.frami.databinding.FragmentWearableConnectedSuccessBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants


class WearableConnectedSuccessFragment :
    BaseFragment<FragmentWearableConnectedSuccessBinding, WearableConnectedSuccessFragmentViewModel>(),
    WearableConnectedSuccessFragmentNavigator {

    private val viewModelInstanceCategory: WearableConnectedSuccessFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentWearableConnectedSuccessBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_wearable_connected_success

    override fun getViewModel(): WearableConnectedSuccessFragmentViewModel =
        viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.WEARABLE) == true) {
                getViewModel().wearableData.set(arguments?.getSerializable(AppConstants.EXTRAS.WEARABLE) as WearableData)
            }

//            log("DATA>>> ${getViewModel().oAuthToken.get()}   ${getViewModel().oAuthTokenVerifier.get()} ${getViewModel().oAuthTokenSecret.get()}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
//        activity?.let {
//            Glide.with(it).asGif().load(R.drawable.download).into(mViewBinding!!.ivGif)
//        };

        mViewBinding!!.tvSuccessMessage.text = getString(
            R.string.your_device_is_connected_successfully,
            getViewModel().wearableData.get()?.name
        )

        Handler(Looper.getMainLooper()).postDelayed({
            onBack()
        }, 5000)
    }

    private fun toolbar() {
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        activity?.finish()
    }

    private fun clickListener() {
        mViewBinding!!.tvSkip.setOnClickListener {
            getViewModel().saveIsWearableDeviceSkip(true)
            onBack()
        }
//        mViewBinding!!.tvYes.setOnClickListener {
//            getViewModel().isSync.set(true)
//            Handler(Looper.getMainLooper()).postDelayed({
//                onBack()
//            }, 5000)
//        }
//        mViewBinding!!.tvNo.setOnClickListener { onBack() }
    }
}