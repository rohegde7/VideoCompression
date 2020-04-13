package com.rohegde7.videocompression.ui.videocompression

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rohegde7.videocompression.livedataenums.VideoCompressionAction
import com.rohegde7.videocompression.util.CompressUtil
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg

class VideoCompressionRepo {

    val mVideoCompressAction = MutableLiveData<VideoCompressionAction>()

    fun compressVideo(
        bitrate: String,
        filePath: String,
        outputFilePath: String,
        isKbps: Boolean,
        mFFmpeg: FFmpeg
    ) {
        val command = CompressUtil.getFfpmpegBitrateCompressionCommand(
            bitrate,
            if (isKbps) "K" else "M",
            filePath,
            outputFilePath
        )

        mFFmpeg.execute(command, object : ExecuteBinaryResponseHandler() {

            override fun onStart() {
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
                Log.e("onFinish", "")
            }
        })
    }
}