package com.doggetdrunk.meetjyou_android.ui.custom.chips

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.doggetdrunk.meetjyou_android.R

class ChipsGridAdapter(
    private val context: Context,
    private val chips: MutableList<ChipItem>,
    private val isSelectable: Boolean = true,
    private val onChipClick: ((ChipItem, Int) -> Unit)? = null
) : BaseAdapter() {

    override fun getCount(): Int = chips.size

    override fun getItem(position: Int): ChipItem = chips[position]

    override fun getItemId(position: Int): Long = chips[position].id.hashCode().toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val chipView = convertView ?: createChipView()
        val chip = chips[position]

        setupChipView(chipView, chip, position)

        return chipView
    }

    private fun createChipView(): View {
        // FrameLayout 컨테이너 생성
        val container = FrameLayout(context).apply {
            layoutParams = AbsListView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // 마진 설정
            val margin = dpToPx(4)
            if (layoutParams is ViewGroup.MarginLayoutParams) {
                (layoutParams as ViewGroup.MarginLayoutParams).setMargins(margin, margin, margin, margin)
            }

            // 클릭 가능하게 설정
            isClickable = true
            isFocusable = true

            // 리플 효과 (API 21+)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                foreground = ContextCompat.getDrawable(context, android.R.drawable.list_selector_background)
            }
        }

        // TextView 생성
        val textView = TextView(context).apply {
            id = View.generateViewId()

            // 패딩 설정
            val horizontalPadding = dpToPx(16)
            val verticalPadding = dpToPx(10)
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

            // 텍스트 스타일
            textSize = 14f
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END

            // 레이아웃 파라미터
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }

        container.addView(textView)
        return container
    }

    private fun setupChipView(chipView: View, chip: ChipItem, position: Int) {
        val container = chipView as FrameLayout
        val textView = container.getChildAt(0) as TextView

        // 텍스트 설정
        textView.text = chip.text

        // 스타일 적용
        if (isSelectable && chip.isSelected) {
            // 선택된 상태: 파란색 테두리 + 파란색 텍스트
            container.background = createSelectedBackground()
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary50))
        } else {
            // 기본 상태: 검은색 테두리 + 검은색 텍스트
            container.background = createDefaultBackground()
            textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        // 클릭 이벤트
        container.setOnClickListener {
            if (isSelectable) {
                chip.isSelected = !chip.isSelected
                setupChipView(chipView, chip, position)
            }
            onChipClick?.invoke(chip, position)
        }
    }

    private fun createDefaultBackground(): Drawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.TRANSPARENT)
            setStroke(dpToPx(1), ContextCompat.getColor(context, R.color.black))
            cornerRadius = dpToPx(20).toFloat()
        }
    }

    private fun createSelectedBackground(): Drawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.TRANSPARENT)
            setStroke(dpToPx(1), ContextCompat.getColor(context, R.color.colorSecondary50))
            cornerRadius = dpToPx(20).toFloat()
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    // 기존 메서드들 동일
    fun getSelectedChips(): List<ChipItem> = chips.filter { it.isSelected }

    fun clearSelection() {
        chips.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    fun setChipSelected(chipId: String, selected: Boolean) {
        chips.find { it.id == chipId }?.let {
            it.isSelected = selected
            notifyDataSetChanged()
        }
    }

    fun updateChips(newChips: List<ChipItem>) {
        chips.clear()
        chips.addAll(newChips)
        notifyDataSetChanged()
    }

    fun addChip(chip: ChipItem) {
        chips.add(chip)
        notifyDataSetChanged()
    }

    fun removeChip(chipId: String) {
        chips.removeAll { it.id == chipId }
        notifyDataSetChanged()
    }
}