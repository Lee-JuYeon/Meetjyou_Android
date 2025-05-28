package com.doggetdrunk.meetjyou_android.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

    @BindingAdapter("android:src")
    @JvmStatic
    fun setImageResource(imageView: ImageView, resourceId: Int) {
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        }
    }

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
                .into(imageView)
        }
    }
}