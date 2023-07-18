package com.frami.ui.videoplayer.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.databinding.FragmentExoVideoPlayerBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util


class ExoVideoPlayerFragment :
    BaseFragment<FragmentExoVideoPlayerBinding, ExoVideoPlayerFragmentViewModel>(),
    ExoVideoPlayerFragmentNavigator {

    private val viewModelInstance: ExoVideoPlayerFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentExoVideoPlayerBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_exo_video_player

    override fun getViewModel(): ExoVideoPlayerFragmentViewModel = viewModelInstance

    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerView: PlayerView

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private lateinit var mediaItem: MediaItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstants.EXTRAS.VIDEO_URL)) {
                getViewModel().videoUrl.set(requireArguments().getString(AppConstants.EXTRAS.VIDEO_URL))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.black)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        playerView = mViewBinding!!.playerView
        dataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), getString(R.string.app_name))
        )

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen =
                savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying =
                savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }

        toolbar()
        handleBackPress()
        clickListener()
        init()

    }

    private fun init() {
        initPlayer()
    }


    private fun toolbar() {
//        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.video)
//        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
//        mViewBinding!!.toolBarLayout.cvSearch.hide()
//        mViewBinding!!.toolBarLayout.cvNotification.hide()
//        mViewBinding!!.toolBarLayout.cvBack.visible()
//        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
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
        requireActivity().finish()
    }

    private fun clickListener() {
        mViewBinding!!.ibClose.setOnClickListener { onBack() }
    }

    private fun initPlayer() {
        log("getViewModel().videoUrl.get() ${getViewModel().videoUrl.get()}")
        mediaItem = MediaItem.Builder()
            .setUri(getViewModel().videoUrl.get())
            .setMimeType(MimeTypes.VIDEO_AV1)
            .setMimeType(MimeTypes.VIDEO_MP4)
            .setMimeType(MimeTypes.VIDEO_FLV)
            .setMimeType(MimeTypes.VIDEO_MP4V)
            .build()
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer
    }

    private fun releasePlayer() {
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentWindowIndex
        exoPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentWindowIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
//            initPlayer()
            playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
//            initPlayer()
            playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    companion object {
        const val HLS_STATIC_URL =
            "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
        const val STATE_RESUME_WINDOW = "resumeWindow"
        const val STATE_RESUME_POSITION = "resumePosition"
        const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
        const val STATE_PLAYER_PLAYING = "playerOnPlay"
    }
}