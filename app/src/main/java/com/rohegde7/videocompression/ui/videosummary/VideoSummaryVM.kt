package com.rohegde7.videocompression.ui.videosummary

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rohegde7.videocompression.contants.VIDEO_URI

class VideoSummaryVM : ViewModel() {

    val mRepository = VideoSummaryRepo()

    lateinit var mVideoUri: Uri

    fun extractIntentExtra(data: Intent) {
        mVideoUri = data.getParcelableExtra(VIDEO_URI)!!
    }
}