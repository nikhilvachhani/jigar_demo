package com.frami.ui.dashboard.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.frami.databinding.ListItemFeedImagesBinding
import com.frami.utils.extensions.layoutInflater
import com.frami.utils.extensions.onClick
import java.util.*

class FeedImageViewPagerAdapter(
    val imageList: List<String>,
    val mListener: OnItemClickListener?
) : PagerAdapter() {
    interface OnItemClickListener {
        fun onActivityViewPagerItemPress()
    }
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as AppCompatImageView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ListItemFeedImagesBinding.inflate(container.context.layoutInflater,container,false)
        binding.activityPhotos = imageList[position]
        Objects.requireNonNull(container).addView(binding.root)
        binding.root.onClick {
            mListener?.onActivityViewPagerItemPress()
        }
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as AppCompatImageView)
    }
}