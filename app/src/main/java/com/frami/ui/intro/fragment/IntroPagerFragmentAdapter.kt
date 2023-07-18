package com.frami.ui.intro.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.frami.data.model.intro.IntroModel
import com.frami.ui.intro.fragment.slider.IntroSliderFragment

class IntroPagerFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private var mIntroData: List<IntroModel>? = null
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        return IntroSliderFragment.getInstance(mIntroData!![position], position)
    }

    override fun getItemCount(): Int {
        return if (mIntroData != null) mIntroData!!.size else 2
    }

    fun setIntroModels(data: List<IntroModel>?) {
        mIntroData = data
        notifyDataSetChanged()
    }
}