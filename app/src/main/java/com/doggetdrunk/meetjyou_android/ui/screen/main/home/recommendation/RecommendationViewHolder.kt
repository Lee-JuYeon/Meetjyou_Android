package com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.databinding.HolderRecommendationBinding

class RecommendationViewHolder(
    private val binding : HolderRecommendationBinding
) : RecyclerView.ViewHolder(binding.root){

    fun bind(model : RecommendationModel, clickListener: IRecommendationHolderClickListener<RecommendationModel>){
        binding.apply {
            this.model = model
            this.listener = clickListener
            executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup) : RecommendationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderRecommendationBinding.inflate(layoutInflater, parent, false)
            return RecommendationViewHolder(binding)
        }
    }
}