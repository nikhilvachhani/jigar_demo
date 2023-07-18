package com.frami.ui.settings.preferences.map

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.databinding.FragmentMapVisibilityPreferenceBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.rewards.details.MenuListAdapter
import com.frami.ui.settings.preferences.map.dialog.MapVisibilityOption1Dialog
import com.frami.ui.settings.preferences.map.dialog.MapVisibilityOption2Dialog
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class MapVisibilityPreferenceFragment :
    BaseFragment<FragmentMapVisibilityPreferenceBinding, MapVisibilityPreferenceFragmentViewModel>(),
    MapVisibilityPreferenceFragmentNavigator, MenuListAdapter.OnItemClickListener {

    private val viewModelInstance: MapVisibilityPreferenceFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentMapVisibilityPreferenceBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_map_visibility_preference

    override fun getViewModel(): MapVisibilityPreferenceFragmentViewModel = viewModelInstance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        mViewBinding!!.recyclerView.adapter =
            MenuListAdapter(
                getViewModel().getMapVisibilityPreferenceMenu(requireActivity()),
                this
            )
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
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
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
    }

    override fun onItemPress(data: IdNameData) {
        when (data.value) {
            getString(R.string.map_visibility_option_1) -> {
                val dialog =
                    MapVisibilityOption1Dialog(
                        requireActivity()
                    )
                dialog.show()
            }
            getString(R.string.map_visibility_option_2) -> {
                val dialog =
                    MapVisibilityOption2Dialog(
                        requireActivity()
                    )
                dialog.show()
            }
        }
    }

}