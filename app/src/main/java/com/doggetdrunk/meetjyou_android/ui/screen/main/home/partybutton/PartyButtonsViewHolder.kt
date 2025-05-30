package com.doggetdrunk.meetjyou_android.ui.screen.main.home.partybutton

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.databinding.HolderPartybuttonBinding

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
        itemCount: Int // <-- 아이템 총 개수를 전달받음
    ) {
        binding.apply {
            this.model = model
            this.listener = clickListener
            executePendingBindings()
        }

        // 화면 높이를 기준으로 균등 분할
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
