package com.doggetdrunk.meetjyou_android.ui.custom.chips

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

class ChipsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridView(context, attrs, defStyleAttr) {

    private var chipsAdapter: ChipsGridAdapter? = null

    init {
        setupGridView()
    }

    private fun setupGridView() {
        // GridView 설정
        numColumns = AUTO_FIT
        columnWidth = dpToPx(80) // 최소 칩 너비
        horizontalSpacing = dpToPx(8)
        verticalSpacing = dpToPx(8)
        stretchMode = STRETCH_SPACING_UNIFORM

        // 패딩 설정
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
        chipsAdapter = ChipsGridAdapter(context, chips.toMutableList(), true, onChipClick)
        adapter = chipsAdapter
    }

    fun setupNonSelectableChips(
        chips: List<ChipItem>,
        onChipClick: ((ChipItem, Int) -> Unit)? = null
    ) {
        chipsAdapter = ChipsGridAdapter(context, chips.toMutableList(), false, onChipClick)
        adapter = chipsAdapter
    }

    // 편의 메서드들
    fun getSelectedChips(): List<ChipItem> = chipsAdapter?.getSelectedChips() ?: emptyList()
    fun clearSelection() = chipsAdapter?.clearSelection()
    fun setChipSelected(chipId: String, selected: Boolean) = chipsAdapter?.setChipSelected(chipId, selected)
    fun updateChips(newChips: List<ChipItem>) = chipsAdapter?.updateChips(newChips)
    fun addChip(chip: ChipItem) = chipsAdapter?.addChip(chip)
    fun removeChip(chipId: String) = chipsAdapter?.removeChip(chipId)
}