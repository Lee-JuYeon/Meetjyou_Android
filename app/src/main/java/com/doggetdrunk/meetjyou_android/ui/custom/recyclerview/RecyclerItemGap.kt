package com.doggetdrunk.meetjyou_android.ui.custom.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemGap(
    private val verticalSpace: Int = 0,
    private val horizontalSpace: Int = 0,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        val layoutManager = parent.layoutManager as? LinearLayoutManager
        val orientation = layoutManager?.orientation ?: LinearLayoutManager.VERTICAL

        when (orientation) {
            LinearLayoutManager.VERTICAL -> {
                // 수직 레이아웃
                if (includeEdge) {
                    outRect.top = if (position == 0) verticalSpace else 0
                    outRect.bottom = verticalSpace
                    outRect.left = horizontalSpace
                    outRect.right = horizontalSpace
                } else {
                    outRect.bottom = if (position != itemCount - 1) verticalSpace else 0
                    outRect.left = horizontalSpace
                    outRect.right = horizontalSpace
                }
            }
            LinearLayoutManager.HORIZONTAL -> {
                // 수평 레이아웃
                if (includeEdge) {
                    outRect.left = if (position == 0) horizontalSpace else 0
                    outRect.right = horizontalSpace
                    outRect.top = verticalSpace
                    outRect.bottom = verticalSpace
                } else {
                    outRect.right = if (position != itemCount - 1) horizontalSpace else 0
                    outRect.top = verticalSpace
                    outRect.bottom = verticalSpace
                }
            }
        }
    }
}