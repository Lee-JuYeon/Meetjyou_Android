package com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.binding.loadImageFromUrl
import com.doggetdrunk.meetjyou_android.databinding.HolderRecommendationBinding

class RecommendationViewHolder(
    private val binding: HolderRecommendationBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: RecommendationModel, clickListener: IRecommendationHolderClickListener<RecommendationModel>) {
        binding.apply {
            // 이미지 로딩
            image.loadImageFromUrl(model.imagePath)

            // 텍스트 설정
            location.text = "${model.city} | ${model.address}"
            titleText.text = model.title

            // 클릭 리스너
            root.setOnClickListener {
                clickListener.onClick(model)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): RecommendationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderRecommendationBinding.inflate(layoutInflater, parent, false)
            return RecommendationViewHolder(binding)
        }
    }
}