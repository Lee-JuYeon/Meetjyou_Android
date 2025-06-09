package com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.binding.loadImageFromUrl
import com.doggetdrunk.meetjyou_android.binding.loadSelfie
import com.doggetdrunk.meetjyou_android.binding.setImageRes
import com.doggetdrunk.meetjyou_android.databinding.HolderRecruitmentBinding


class RecruitmentViewHolder(
    private val binding: HolderRecruitmentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: RecruitmentModel, clickListener: IRecruitmentHolderClickListener<RecruitmentModel>) {
        binding.apply {
            // 프로필 이미지 로딩
            if (model.userSelfieURL.isNullOrEmpty()) {
                selfie.setImageRes(R.drawable.icon_placeholder_selfie) // placeholder 이미지
            } else {
                selfie.loadSelfie(model.userSelfieURL)
            }

            // 텍스트 설정
            name.text = model.userName
            content.text = model.content

            // 날짜는 현재 모델에 없으므로 빈 값으로 설정하거나 생성 날짜를 추가할 수 있음
            date.text = ""

            // 클릭 리스너들
            root.setOnClickListener {
                clickListener.onHolderClick(model)
            }

            selfie.setOnClickListener {
                clickListener.onProfileClick(model)
            }

            content.setOnClickListener {
                clickListener.onContentClick(model)
            }

            denied.setOnClickListener {
                clickListener.onDenied(model)
            }

            aceept.setOnClickListener {
                clickListener.onAccept(model)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): RecruitmentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderRecruitmentBinding.inflate(layoutInflater, parent, false)
            return RecruitmentViewHolder(binding)
        }
    }
}