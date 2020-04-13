package com.rohegde7.videocompression.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohegde7.videocompression.R
import com.rohegde7.videocompression.pojos.Video

class VideoListAdapter(
    private val context: Context,
    var videoList: List<Video>?,
    private val listener: ClickListener

) : RecyclerView.Adapter<VideoListAdapter.VideoVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVH {
        return VideoVH(
            LayoutInflater.from(context).inflate(R.layout.video_adapter_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return videoList?.size ?: 0
    }

    fun updateVideoList(videoList: List<Video>) {
        this.videoList = videoList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VideoVH, position: Int) {
        val video = videoList?.get(position)

        holder.titleTextView.text = video?.title
        loadImage(video!!.path, holder.imageView)
        setOnImageClickListener(holder.imageView, position)
    }

    private fun loadImage(filePath: String, imageView: ImageView) {
        Glide.with(context)
            .load(filePath)
            .placeholder(R.drawable.ic_landscape_black_24dp)
            .centerCrop()
            .into(imageView);
    }

    private fun setOnImageClickListener(imageView: ImageView, position: Int) {
        imageView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    class VideoVH(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val titleTextView: TextView = view.findViewById(R.id.title_text_view)
    }
}