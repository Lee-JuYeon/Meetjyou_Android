package com.doggetdrunk.meetjyou_android.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doggetdrunk.meetjyou_android.R

object BindingAdapters {

    /**
     * DataBinding에서 사용할 커스텀 BindingAdapter들
     * Top-level 함수로 정의하여 KSP 호환성 확보
     */

    /**
     * ImageView에 리소스 ID로 이미지 설정
     * 사용법: app:imageRes="@{viewModel.imageResId}"
     */
    @BindingAdapter("imageRes")
    fun setImageResource(imageView: ImageView, resourceId: Int) {
        if (resourceId != 0) {
            try {
                imageView.setImageResource(resourceId)
            } catch (e: Exception) {
                // 리소스를 찾을 수 없는 경우 기본 이미지 설정
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    /**
     * ImageView에 URL로 이미지 로딩 (Glide 사용)
     * 사용법: app:imageUrl="@{model.imageUrl}"
     */
    @BindingAdapter("imageUrl")
    fun loadImageFromUrl(imageView: ImageView, url: String?) {
        if (!url.isNullOrEmpty() && url.isNotBlank()) {
            try {
                Glide.with(imageView.context)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            } catch (e: Exception) {
                // Glide 로딩 실패 시 기본 이미지 설정
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}