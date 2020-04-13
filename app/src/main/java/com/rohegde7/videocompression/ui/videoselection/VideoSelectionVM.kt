package com.rohegde7.videocompression.ui.videoselection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohegde7.videocompression.livedataenums.VideoSelectionAction

class VideoSelectionVM : ViewModel() {

    // TODO -> For this screen we do not need a repository as we are not doing any API or DB calls
    val mRepository = VideoSelectionRepo()

    val mVideoSelectionLiveData = MutableLiveData<VideoSelectionAction>()

    fun onSelectVideoButtonClicked() {
        mVideoSelectionLiveData.value = VideoSelectionAction.SELECT_VIDEO_BUTTON_CLICKED
    }
}