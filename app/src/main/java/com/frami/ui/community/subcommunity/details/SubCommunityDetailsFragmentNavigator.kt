package com.frami.ui.community.subcommunity.details

import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.ui.base.BaseNavigator

interface SubCommunityDetailsFragmentNavigator :
    BaseNavigator {
//    fun subCommunityDetailsFetchSuccess(data: SubCommunityData?)
    fun subCommunityDeleteSuccess(message: String)
}