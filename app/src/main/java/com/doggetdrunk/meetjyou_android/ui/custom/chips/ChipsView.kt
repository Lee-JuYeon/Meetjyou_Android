package com.doggetdrunk.meetjyou_android.ui.custom.chips

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.doggetdrunk.meetjyou_android.R

class ChipsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var chips = mutableListOf<ChipItem>()
    private var isSelectable = true
    private var onChipClick: ((ChipItem, Int) -> Unit)? = null

    // 간격 설정
    private val chipHorizontalSpacing = dpToPx(5) // 5dp로 설정
    private val chipVerticalSpacing = dpToPx(5)   // 5dp로 설정

    init {
        orientation = VERTICAL
        setupPadding()
    }

    private fun setupPadding() {
        val padding = dpToPx(16)
        setPadding(padding, dpToPx(8), padding, dpToPx(8))
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun setupSelectableChips(
        chips: List<ChipItem>,
        onChipClick: ((ChipItem, Int) -> Unit)? = null
    ) {
        this.chips.clear()
        this.chips.addAll(chips)
        this.isSelectable = true
        this.onChipClick = onChipClick
        createChipsLayout()
    }

    fun setupNonSelectableChips(
        chips: List<ChipItem>,
        onChipClick: ((ChipItem, Int) -> Unit)? = null
    ) {
        this.chips.clear()
        this.chips.addAll(chips)
        this.isSelectable = false
        this.onChipClick = onChipClick
        createChipsLayout()
    }

    private fun createChipsLayout() {
        removeAllViews()

        if (chips.isEmpty()) return

        var currentRow: LinearLayout? = null
        var currentRowWidth = 0
        val availableWidth = getAvailableWidth()

        chips.forEachIndexed { index, chip ->
            val chipView = createChipView(chip, index)
            val chipWidth = measureChipWidth(chipView)

            // 새 행이 필요한지 확인
            if (currentRow == null || currentRowWidth + chipWidth + chipHorizontalSpacing > availableWidth) {
                // 새 행 생성
                currentRow = LinearLayout(context).apply {
                    orientation = HORIZONTAL
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        if (this@ChipsView.childCount > 0) {
                            topMargin = chipVerticalSpacing
                        }
                    }
                }
                addView(currentRow)
                currentRowWidth = 0
            }

            // 칩을 현재 행에 추가
            val chipLayoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                if (currentRow?.childCount ?: 0 > 0) {
                    leftMargin = chipHorizontalSpacing
                }
            }

            chipView.layoutParams = chipLayoutParams
            currentRow?.addView(chipView)
            currentRowWidth += chipWidth + if (currentRow?.childCount ?: 0 > 1) chipHorizontalSpacing else 0
        }
    }

    private fun getAvailableWidth(): Int {
        val padding = paddingLeft + paddingRight
        return if (width > 0) {
            width - padding
        } else {
            // 대략적인 화면 너비 사용
            resources.displayMetrics.widthPixels - padding - dpToPx(32) // 여분의 마진 고려
        }
    }

    private fun measureChipWidth(chipView: View): Int {
        chipView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        return chipView.measuredWidth
    }

    private fun createChipView(chip: ChipItem, index: Int): TextView {
        return TextView(context).apply {
            text = chip.text

            // 텍스트 스타일
            textSize = 14f
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END

            // 패딩 설정 (텍스트에 맞게 조정)
            val horizontalPadding = dpToPx(16)
            val verticalPadding = dpToPx(10)
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

            // 스타일 적용
            updateChipStyle(this, chip)

            // 클릭 리스너
            setOnClickListener {
                if (isSelectable) {
                    chip.isSelected = !chip.isSelected
                    updateChipStyle(this, chip)
                }
                onChipClick?.invoke(chip, index)
            }

            // 클릭 효과
            isClickable = true
            isFocusable = true
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                foreground = ContextCompat.getDrawable(context, android.R.drawable.list_selector_background)
            }
        }
    }

    private fun updateChipStyle(textView: TextView, chip: ChipItem) {
        if (isSelectable && chip.isSelected) {
            // 선택된 상태: 파란색 테두리 + 파란색 텍스트
            textView.background = createSelectedBackground()
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary50))
        } else {
            // 기본 상태: 검은색 테두리 + 검은색 텍스트
            textView.background = createDefaultBackground()
            textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }

    private fun createDefaultBackground(): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.TRANSPARENT)
            setStroke(dpToPx(1), Color.parseColor("#CCCCCC"))
            cornerRadius = dpToPx(8).toFloat()
        }
    }

    private fun createSelectedBackground(): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.TRANSPARENT)
            setStroke(dpToPx(1), ContextCompat.getColor(context, R.color.colorSecondary50))
            cornerRadius = dpToPx(8).toFloat()
        }
    }

    // 편의 메서드들
    fun getSelectedChips(): List<ChipItem> = chips.filter { it.isSelected }

    fun clearSelection() {
        chips.forEach { it.isSelected = false }
        createChipsLayout()
    }

    fun setChipSelected(chipId: String, selected: Boolean) {
        chips.find { it.id == chipId }?.let {
            it.isSelected = selected
            createChipsLayout()
        }
    }

    fun updateChips(newChips: List<ChipItem>) {
        chips.clear()
        chips.addAll(newChips)
        createChipsLayout()
    }

    fun addChip(chip: ChipItem) {
        chips.add(chip)
        createChipsLayout()
    }

    fun removeChip(chipId: String) {
        chips.removeAll { it.id == chipId }
        createChipsLayout()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 크기가 변경되면 레이아웃 재생성
        if (chips.isNotEmpty()) {
            post { createChipsLayout() }
        }
    }
}