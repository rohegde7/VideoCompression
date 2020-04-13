package com.rohegde7.videocompression.ui.videoselection

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.GridLayoutManager
import com.rohegde7.videocompression.R
import com.rohegde7.videocompression.adapter.ClickListener
import com.rohegde7.videocompression.adapter.VideoListAdapter
import com.rohegde7.videocompression.contants.COMPRESS_VIDEO
import com.rohegde7.videocompression.contants.SELECTED_VIDEO_FILE_PATH
import com.rohegde7.videocompression.databinding.ActivityVideoSelectionBinding
import com.rohegde7.videocompression.livedataenums.VideoSelectionAction
import com.rohegde7.videocompression.pojos.Video
import com.rohegde7.videocompression.ui.videocompression.VideoCompressionActivity
import com.rohegde7.videocompression.util.PermissionUtil
import com.rohegde7.videocompression.util.UiUtil
import kotlinx.android.synthetic.main.activity_video_selection.*


class VideoSelectionActivity : AppCompatActivity(), ClickListener {

    val mViewModel: VideoSelectionVM = NewInstanceFactory().create(VideoSelectionVM::class.java)

    lateinit var mBinding: ActivityVideoSelectionBinding

    val mAdapter = VideoListAdapter(this, null, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpDataBinding()
        observeViewModelLiveData()
    }

    override fun onResume() {
        super.onResume()

        if (PermissionUtil.checkPermissions(this)) initRecyclerView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (permission in grantResults)
            if (permission != PERMISSION_GRANTED) {
                showPermissionNotGrantedToast()
                return
            }

        initRecyclerView()
    }

    private fun showPermissionNotGrantedToast() {
        Toast.makeText(
            this,
            "Please provide appropriate permissions to continue!",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            COMPRESS_VIDEO -> fetchAllVideosFromStorage()
        }
    }

    override fun onItemClick(position: Int) {
        startVideoCompressionActivity(mAdapter.videoList!!.get(position).path)
    }

    private fun startVideoCompressionActivity(selectedFilePath: String) {
        val compressVideoActivityIntent = Intent(this, VideoCompressionActivity::class.java)
        compressVideoActivityIntent.putExtra(SELECTED_VIDEO_FILE_PATH, selectedFilePath)

        startActivityForResult(
            compressVideoActivityIntent,
            COMPRESS_VIDEO
        )
    }

    private fun setUpDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_selection)
        mBinding.vm = mViewModel
    }

    private fun observeViewModelLiveData() {
        mViewModel.mVideoSelectionLiveData.observe(this, Observer {

            when (it) {
                VideoSelectionAction.SELECT_VIDEO_BUTTON_CLICKED -> fetchAllVideosFromStorage()
                null -> UiUtil.showUnexpectedErrorToast()
            }
        })
    }

    private fun fetchAllVideosFromStorage() {
        val videoList = ArrayList<Video>()

        val contentResolver = contentResolver
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val filePath: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val title: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))

                val video = Video(filePath, title)
                videoList.add(video)

            } while (cursor.moveToNext())
        }

        mAdapter.updateVideoList(videoList)
    }

    private fun initRecyclerView() {
        fetchAllVideosFromStorage()
        video_recycler_view.layoutManager = GridLayoutManager(this, 2)
        video_recycler_view.adapter = mAdapter
    }
}
