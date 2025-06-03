package com.doggetdrunk.meetjyou_android.ui.screen.main.home.hotpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.binding.loadImageFromUrl
import com.doggetdrunk.meetjyou_android.databinding.HolderHotpostBinding
import com.doggetdrunk.meetjyou_android.model.PartyModel

class HotpostViewHolder(
    private val binding: HolderHotpostBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: PartyModel, clickListener: IHotpostHolderClickListener<PartyModel>) {
        binding.apply {
            // 이미지 로딩
            image.loadImageFromUrl(model.imageURL)

            // 텍스트 설정
            title.text = model.partyTitle
            viewCount.text = root.context.getString(R.string.view_count_format, model.viewerCountToText)

            // 클릭 리스너
            root.setOnClickListener {
                clickListener.onClick(model)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): HotpostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderHotpostBinding.inflate(layoutInflater, parent, false)
            return HotpostViewHolder(binding)
        }
    }
}