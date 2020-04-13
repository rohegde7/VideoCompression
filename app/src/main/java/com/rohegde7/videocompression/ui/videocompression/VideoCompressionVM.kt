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

    private val mRepository = VideoCompressionRepo()

    val mVideoCompressAction: MutableLiveData<VideoCompressionAction>
        get() = mRepository.mVideoCompressAction

    lateinit var mVideoUri: Uri

    lateinit var mFilePath: String

    //  I currently have hard coded the value
    val mBitrate = ObservableField("540")

    //  I have currently made this as true, but later will have to make it false
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

    private fun createFileIfItDoesNotExists() {
        val file = File(mOutputFileLocation)
        if (!file.exists()) file.mkdir()
    }

    fun onCompressionInitiated() {
        createFileIfItDoesNotExists()

        mRepository.compressVideo(
            mBitrate.get()!!,
            mVideoUri.toFile().absolutePath,
            "$mOutputFileLocation/${File(mFilePath).name}",
            isKbps.get(),
            mFFmpeg
        )
    }
}