package com.rohegde7.videocompression.ui.videoselection

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.rohegde7.videocompression.pojos.Video

class VideoSelectionRepo {

    fun fetchAllVideosFromStorage(contentResolver: ContentResolver): List<Video> {
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

        return videoList
    }
}