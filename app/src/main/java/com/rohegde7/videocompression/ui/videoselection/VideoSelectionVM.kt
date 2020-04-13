package com.rohegde7.videocompression.ui.videoselection

import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohegde7.videocompression.livedataenums.VideoSelectionAction
import com.rohegde7.videocompression.pojos.Video

class VideoSelectionVM : ViewModel() {

    val mRepository = VideoSelectionRepo()

    val mVideoSelectionLiveData = MutableLiveData<VideoSelectionAction>()

    fun onSelectVideoButtonClicked() {
        mVideoSelectionLiveData.value = VideoSelectionAction.SELECT_VIDEO_BUTTON_CLICKED
    }

    fun fetchAllVideosFromStorage(contentResolver: ContentResolver) : List<Video> {
        return mRepository.fetchAllVideosFromStorage(contentResolver)
    }
}