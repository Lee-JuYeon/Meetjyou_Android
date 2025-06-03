package com.doggetdrunk.meetjyou_android.binding

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doggetdrunk.meetjyou_android.R

/**
 * ImageView 확장 함수들 - DataBinding 대신 사용
 */

/**
 * 리소스 ID로 이미지 설정
 */
fun ImageView.setImageRes(resourceId: Int) {
    if (resourceId != 0) {
        try {
            setImageResource(resourceId)
        } catch (e: Exception) {
            setImageResource(R.drawable.ic_launcher_background)
        }
    } else {
        setImageResource(R.drawable.ic_launcher_background)
    }
}

/**
 * URL로 이미지 로딩
 */
fun ImageView.loadImageFromUrl(url: String?) {
    if (!url.isNullOrEmpty() && url.isNotBlank()) {
        try {
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
        } catch (e: Exception) {
            setImageResource(R.drawable.ic_launcher_background)
        }
    } else {
        setImageResource(R.drawable.ic_launcher_background)
    }
}