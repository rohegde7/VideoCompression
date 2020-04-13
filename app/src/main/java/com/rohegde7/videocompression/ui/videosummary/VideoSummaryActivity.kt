package com.rohegde7.videocompression.ui.videosummary

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.rohegde7.videocompression.R
import com.rohegde7.videocompression.databinding.ActivityVideoSummaryBinding
import kotlinx.android.synthetic.main.activity_video_compression.*

class VideoSummaryActivity : AppCompatActivity() {

    val mViewModel: VideoSummaryVM =
        ViewModelProvider.NewInstanceFactory().create(VideoSummaryVM::class.java)

    lateinit var mBinding: ActivityVideoSummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.extractIntentExtra(intent)
        setUpDataBinding()
        observeViewModelLiveData()
        initVideoView()
    }

    private fun setUpDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_summary)
        mBinding.vm = mViewModel
    }

    private fun observeViewModelLiveData() {

    }

    private fun initVideoView() {
        val mediaController: MediaController = MediaController(this);
        mediaController.setAnchorView(video_view)
        video_view.setMediaController(mediaController);

        video_view.setVideoURI(mViewModel.mVideoUri)
        video_view.requestFocus()
        video_view.start()

        initVideoViewListeners()
    }

    private fun initVideoViewListeners() {
        setVideoViewOnErrorListener()
    }

    private fun setVideoViewOnErrorListener() {
        video_view.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
            Toast.makeText(
                this,
                applicationContext.getString(R.string.unable_to_play_video),
                Toast.LENGTH_SHORT
            ).show()

            false
        })
    }
}
