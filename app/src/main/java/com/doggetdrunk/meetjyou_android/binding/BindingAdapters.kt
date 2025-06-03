package com.doggetdrunk.meetjyou_android.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doggetdrunk.meetjyou_android.R

object BindingAdapters {

    /**
     * DataBinding에서 사용할 커스텀 BindingAdapter들
     * KSP 호환성을 위해 object 대신 companion object 또는 top-level 함수 사용
     */

    /**
     * ImageView에 리소스 ID로 이미지 설정
     * 사용법: app:imageRes="@{viewModel.imageResId}"
     */
    @BindingAdapter("imageRes")
    fun setImageResource(imageView: ImageView, resourceId: Int) {
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        }
    }

    /**
     * ImageView에 URL로 이미지 로딩 (Glide 사용)
     * 사용법: app:imageUrl="@{model.imageUrl}"
     */
    @BindingAdapter("imageUrl")
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