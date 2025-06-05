package com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.databinding.HolderNotifyBinding


class NotifyViewHolder(
    private val binding: HolderNotifyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: NotifyModel, clickListener: INotifyHolderClickListener<NotifyModel>) {
        binding.apply {
            // 텍스트 설정
            title.text = model.title
            date.text = model.formattedDate

            // 클릭 리스너
            root.setOnClickListener {
                clickListener.onClick(model)
            }

//            // 삭제 버튼 (만약 있다면)
//            deleteBtn?.setOnClickListener {
//                clickListener.onDelete(model)
//            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): NotifyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderNotifyBinding.inflate(layoutInflater, parent, false)
            return NotifyViewHolder(binding)
        }
    }
}