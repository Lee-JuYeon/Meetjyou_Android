package com.doggetdrunk.meetjyou_android.ui.screen.main.home.hotpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.databinding.HolderHotpostBinding
import com.doggetdrunk.meetjyou_android.model.PartyModel

class HotpostViewHolder(
    private val binding : HolderHotpostBinding
) : RecyclerView.ViewHolder(binding.root){

    fun bind(model : PartyModel, clickListener: IHotpostHolderClickListener<PartyModel>){
        binding.apply {
            this.model = model
            this.listener = clickListener
            executePendingBindings()
        }
    }

    companion object {
        fun create(parent : ViewGroup) : HotpostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderHotpostBinding.inflate(layoutInflater, parent, false)
            return HotpostViewHolder(binding)
        }
    }
}