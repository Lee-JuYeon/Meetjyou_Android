package com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.binding.loadImageFromUrl
import com.doggetdrunk.meetjyou_android.binding.setImageRes
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.HolderApplyBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ApplyViewHolder(
    private val binding: HolderApplyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: ApplyModel, clickListener: IApplyHolderClickListener<ApplyModel>) {
        binding.apply {
            // 썸네일 이미지 로딩
            if (model.partyThumbNailURL.isNullOrEmpty()) {
                thumbnail.setImageRes(R.drawable.icon_placeholder_post) // placeholder 이미지
            } else {
                thumbnail.loadImageFromUrl(model.partyThumbNailURL)
            }

            // 텍스트 설정
            title.text = model.partyTitle
            content.text = model.partyContent

            // 날짜 포맷팅
            val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            // content 아래에 날짜 표시를 위한 별도 TextView가 필요하지만 레이아웃에 없으므로 생략

            // 상태에 따른 버튼 설정
            when (model.returnType) {
                ApplyReturnType.WAIT -> {
                    left.text = "대기중"
                    right.text = "취소"
                    left.setBackgroundResource(R.drawable.background_denied)
                    right.setBackgroundResource(R.drawable.background_denied)
                    left.visibility = View.VISIBLE
                    right.visibility = View.VISIBLE
                }
                ApplyReturnType.ACCEPT -> {
                    left.text = "승인됨"
                    right.text = "확인"
                    left.setBackgroundResource(R.drawable.background_accept)
                    right.setBackgroundResource(R.drawable.background_denied)
                    left.visibility = View.VISIBLE
                    right.visibility = View.VISIBLE
                }
                ApplyReturnType.DENIED -> {
                    left.text = "거절됨"
                    right.text = "확인"
                    left.setBackgroundResource(R.drawable.background_denied)
                    right.setBackgroundResource(R.drawable.background_denied)
                    left.visibility = View.VISIBLE
                    right.visibility = View.VISIBLE
                }
            }

            // 클릭 리스너들
            root.setOnClickListener {
                clickListener.onPostClick(model)
            }

            left.setOnClickListener {
                clickListener.onButtonClicm(model.returnType)
            }

            right.setOnClickListener {
                when (model.returnType) {
                    ApplyReturnType.WAIT -> clickListener.onButtonClicm(ApplyReturnType.DENIED) // 취소
                    else -> clickListener.onButtonClicm(ApplyReturnType.ACCEPT) // 확인
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): ApplyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderApplyBinding.inflate(layoutInflater, parent, false)
            return ApplyViewHolder(binding)
        }
    }
}