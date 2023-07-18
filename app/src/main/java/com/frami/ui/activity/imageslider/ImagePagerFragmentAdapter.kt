package com.frami.ui.activity.imageslider

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagePagerFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var imageUrlList: List<String>? = null
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        return ImageSliderFragment.getInstance(imageUrlList!![position], position)
    }

    override fun getItemCount(): Int {
        return if (imageUrlList != null) imageUrlList!!.size else 2
    }

    fun setImageList(imageList : List<String>){
        this.imageUrlList = imageList
        notifyDataSetChanged()
    }
}