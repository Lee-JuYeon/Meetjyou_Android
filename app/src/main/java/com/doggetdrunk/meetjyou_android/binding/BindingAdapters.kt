package com.doggetdrunk.meetjyou_android.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doggetdrunk.meetjyou_android.R

object BindingAdapters {


    @BindingAdapter("imageRes")
    @JvmStatic
    fun setImageResource2(imageView: ImageView, resourceId: Int) {
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        }
    }


    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}