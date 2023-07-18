package com.frami.ui.common.fullscreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.frami.ui.common.fullscreen.fragment.slider.FullScreenImageSliderFragment

class FullScreenImagePagerFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var imageUrlList: List<String>? = null
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        return FullScreenImageSliderFragment.getInstance(imageUrlList!![position], position)
    }

    override fun getItemCount(): Int {
        return if (imageUrlList != null) imageUrlList!!.size else 2
    }
}