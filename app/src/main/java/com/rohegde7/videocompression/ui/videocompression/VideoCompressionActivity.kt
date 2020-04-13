package com.rohegde7.videocompression.ui.videocompression

import android.content.Intent
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rohegde7.videocompression.R
import com.rohegde7.videocompression.contants.START_SUMMARY_ACTIVITY
import com.rohegde7.videocompression.contants.VIDEO_URI
import com.rohegde7.videocompression.databinding.ActivityVideoCompressionBinding
import com.rohegde7.videocompression.livedataenums.VideoCompressionAction
import com.rohegde7.videocompression.ui.videosummary.VideoSummaryActivity
import com.rohegde7.videocompression.util.UiUtil
import kotlinx.android.synthetic.main.activity_video_compression.*
import nl.bravobit.ffmpeg.FFmpeg

class VideoCompressionActivity : AppCompatActivity() {

    val mViewModel: VideoCompressionVM =
        ViewModelProvider.NewInstanceFactory().create(VideoCompressionVM::class.java)

    lateinit var mBinding: ActivityVideoCompressionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.extractIntentExtras(intent)
        setUpDataBinding()
        observeViewModelLiveData()
        initVideoView()
        initFfmpeg()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            START_SUMMARY_ACTIVITY -> finish()
        }
    }

    private fun setUpDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_compression)
        mBinding.vm = mViewModel
    }

    private fun observeViewModelLiveData() {
        mViewModel.mVideoCompressAction.observe(this, Observer {

            when (it) {
                VideoCompressionAction.COMPRESS_REQUESTED -> validateAndCompressVideo()
                VideoCompressionAction.DISPLAY_PROGRESS -> displayCompressionInProgress()
                VideoCompressionAction.COMPRESS_SUCCESS -> onCompressionSuccess()
            }
        })
    }

    private fun displayCompressionInProgress() {
        UiUtil.displayProgress(
            this,
            VideoCompressionAction.DISPLAY_PROGRESS.message
        )
    }

    private fun onCompressionSuccess() {
        UiUtil.hideProgress()
        Toast.makeText(this, "Compression is successful!", Toast.LENGTH_SHORT).show()
        startSummaryActivity()
    }

    private fun startSummaryActivity() {
        val intent = Intent(this, VideoSummaryActivity::class.java)
        intent.putExtra(VIDEO_URI, mViewModel.mVideoUri)

        startActivityForResult(intent, START_SUMMARY_ACTIVITY)
    }

    private fun validateAndCompressVideo() {
        if ((kbps_radio_button.isChecked || mbps_radio_button.isChecked)
            && mViewModel.mBitrate.get()!!.isNotBlank()
        ) {
            mViewModel.onCompressionInitiated()

        } else Toast.makeText(
            this,
            applicationContext.getString(R.string.please_enter_bitrate_and_its_unit),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initFfmpeg() {
        mViewModel.mFFmpeg = FFmpeg.getInstance(this)
        checkIfDeviceSupportsCompression()
    }

    private fun checkIfDeviceSupportsCompression() {
        if (!mViewModel.mFFmpeg.isSupported) {
            Toast.makeText(
                this,
                "Your device does not support FfMpeg compression library! SAD :|",
                Toast.LENGTH_LONG
            ).show()

            finish()
        }
    }

    private fun initVideoView() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(video_view)

        video_view.setMediaController(mediaController)
        video_view.setVideoURI(mViewModel.mVideoUri)
        video_view.requestFocus()   // TODO -> required?
        video_view.start()
    }
}
