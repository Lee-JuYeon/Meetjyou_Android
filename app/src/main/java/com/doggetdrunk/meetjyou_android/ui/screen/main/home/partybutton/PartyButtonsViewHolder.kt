package com.doggetdrunk.meetjyou_android.ui.screen.main.home.partybutton

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.databinding.HolderPartybuttonBinding
import com.doggetdrunk.meetjyou_android.binding.setImageRes

class PartyButtonsViewHolder(
    private val binding: HolderPartybuttonBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val Int.toDp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()

    private val Float.toDp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

    fun bind(
        model: PartyButtonModel,
        clickListener: IPartyButtonHolderClickListener<PartyButtonModel>,
        itemCount: Int
    ) {
        binding.apply {
            // 이미지 설정
            image.setImageRes(model.imgRes)

            // 텍스트 설정
            title.text = model.title
            description.text = model.description

            // 클릭 리스너
            root.setOnClickListener {
                clickListener.onClick(model)
            }
        }

        // 화면 너비를 기준으로 균등 분할
        val screenWidth = binding.root.resources.displayMetrics.widthPixels
        val itemWidth = screenWidth / itemCount
        binding.root.layoutParams = binding.root.layoutParams.apply {
            width = itemWidth - 22.5f.toDp.toInt()
        }
    }

    companion object {
        fun create(parent: ViewGroup): PartyButtonsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderPartybuttonBinding.inflate(layoutInflater, parent, false)
            return PartyButtonsViewHolder(binding)
        }
    }
}