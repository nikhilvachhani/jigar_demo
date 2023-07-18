package com.frami.ui.post.create

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.post.PostData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class CreatePostFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CreatePostFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var relatedId = ObservableField<String>()
    var postType = ObservableField<String>(AppConstants.POST_TYPE.Challenge)
    var post = ObservableField<PostData>()
    var isEditPost = ObservableBoolean(false)
    var isMediaAdapterEmpty = ObservableBoolean(true)
    var isPartnerCommunity = ObservableBoolean(false)
    var partnerCommunityList = ObservableField<List<CompetitorData>>()
    var selectedPartnerCommunityList = ObservableField<List<CompetitorData>>()
    var selectedPartnerCommunityNames = ObservableField<String>("")

    fun createPost(
        createPostRequest: HashMap<String, Any>,
        postImages: List<File>?,
        thumbnailUri: ArrayList<File>?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createPostRequest $createPostRequest")
        val disposable: Disposable = getDataManager()
            .createPost(createPostRequest, postImages, thumbnailUri)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createPostRequest response>>> ${
                            Gson().toJson(response)
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createPostSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun updatePost(
        createPostRequest: HashMap<String, Any>,
        postImages: List<File>?,
        thumbnailUri: ArrayList<File>?
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updatePostRequest $createPostRequest")
        val disposable: Disposable = getDataManager()
            .updatePost(createPostRequest, postImages, thumbnailUri)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updatePost response>>> ${
                            Gson().toJson(response)
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createPostSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getAcceptedInviteCommunityAPI() {
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
                .getAcceptedInviteCommunityAPI(relatedId.get() ?: "")
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    getNavigator()?.log(
                            "getAcceptedInviteCommunityAPI response>>> ${
                                Gson().toJson(
                                        response
                                )
                            }"
                    )
                    getNavigator()?.communityFetchSuccessfully(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                    getNavigator()!!.hideLoading()
                }, { throwable ->
                    getNavigator()?.communityFetchSuccessfully(ArrayList())
                    getNavigator()?.handleError(throwable)
                    getNavigator()!!.hideLoading()
                })
        mCompositeDisposable.add(disposable)
    }
}