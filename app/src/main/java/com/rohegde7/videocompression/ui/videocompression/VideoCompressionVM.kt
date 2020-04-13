package com.rohegde7.videocompression.ui.videocompression

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohegde7.videocompression.application.VideoCompressionApplication
import com.rohegde7.videocompression.contants.SELECTED_VIDEO_FILE_PATH
import com.rohegde7.videocompression.livedataenums.VideoCompressionAction
import com.rohegde7.videocompression.util.CompressUtil
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import java.io.File

class VideoCompressionVM : ViewModel() {

    // TODO -> For this screen we do not need a repository as we are not doing any API or DB calls
    val mRepository = VideoCompressionRepo()

    val mVideoCompressAction = MutableLiveData<VideoCompressionAction>()

    lateinit var mVideoUri: Uri

    lateinit var mFilePath: String

    // TODO -> remove hard coded values
    val mBitrate = ObservableField("540")

    // TODO -> make this false
    val isKbps = ObservableBoolean(true)    // there are currently only 2 -> KBPS and MBPS

    lateinit var mFFmpeg: FFmpeg

    val mOutputFileLocation =
        "${VideoCompressionApplication.mAppContext.externalMediaDirs[0].absolutePath}/CompressedVideos"

    fun extractIntentExtras(data: Intent) {
        mFilePath = data.getStringExtra(SELECTED_VIDEO_FILE_PATH)!!
        mVideoUri = Uri.fromFile(File(mFilePath))
    }

    fun onCompressButtonClicked() {
        mVideoCompressAction.value = VideoCompressionAction.COMPRESS_REQUESTED
    }

    fun compress() {
        val command = CompressUtil.getFfpmpegBitrateCompressionCommand(
            mBitrate.get()!!,
            if (isKbps.get()) "K" else "M",
            mVideoUri.toFile().absolutePath,
            "$mOutputFileLocation/${File(mFilePath).name}"
        )

        mFFmpeg.execute(command, object : ExecuteBinaryResponseHandler() {

            override fun onStart() {
                createFileIfItDoesNotExists()
                mVideoCompressAction.value = VideoCompressionAction.DISPLAY_PROGRESS

                Log.e("onStart", "")
            }

            override fun onProgress(message: String) {
                Log.e("onProgress", message)
            }

            override fun onFailure(message: String) {
                val action = VideoCompressionAction.FAILURE
                action.message = "message"

                Log.e("onFailure", message)
            }

            override fun onSuccess(message: String) {
                mVideoCompressAction.value = VideoCompressionAction.COMPRESS_SUCCESS

                Log.e("onSuccess", message)
            }

            override fun onFinish() {
//                mVideoCompressAction.value = VideoCompressionAction.COMPRESS_SUCCESS

                Log.e("onFinish", "")
            }
        })
    }

    private fun createFileIfItDoesNotExists() {
        val file = File(mOutputFileLocation)
        if (!file.exists()) file.mkdir()
    }

    fun onCompressionInitiated() {
        compress()
    }
}