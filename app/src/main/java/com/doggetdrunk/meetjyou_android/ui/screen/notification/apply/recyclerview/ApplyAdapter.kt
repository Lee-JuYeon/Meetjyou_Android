package com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseDiffUtil

class ApplyAdapter : RecyclerView.Adapter<ApplyViewHolder>() {

    private val items = ArrayList<ApplyModel>()

    fun updateList(newItems: List<ApplyModel>?) {
        try {
            val diffResult = DiffUtil.calculateDiff(
                object : BaseDiffUtil<ApplyModel>(
                    oldList = items,
                    newList = newItems ?: mutableListOf()
                ) {},
                false
            )

            diffResult.dispatchUpdatesTo(this)
            items.clear()
            items.addAll(newItems ?: mutableListOf())
        } catch (e: Exception) {
            Log.e("mException", "ApplyAdapter, updateList // Exception : ${e.message}")
        }
    }

    private var clickListener: IApplyHolderClickListener<ApplyModel>? = null
    fun setOnClickListener(listener: IApplyHolderClickListener<ApplyModel>) {
        this.clickListener = listener
    }

    // ViewHolder 재사용을 위한 풀 사이즈 증가
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].uid.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplyViewHolder =
        ApplyViewHolder.create(parent)

    override fun onBindViewHolder(holder: ApplyViewHolder, position: Int) {
        try {
            val item = items.getOrNull(position) ?: return
            val safeClickListener = clickListener ?: object :
                IApplyHolderClickListener<ApplyModel> {
                override fun onPostClick(model: ApplyModel) {
                    // 기본 동작 없음
                }
                override fun onButtonClicm(type: ApplyReturnType) {
                    // 기본 동작 없음
                }
            }
            holder.bind(item, safeClickListener)
        } catch (e: Exception) {
            Log.e("mException", "ApplyAdapter, onBindViewHolder // Exception : ${e.message}")
        }
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: ApplyViewHolder) {
        try {
            super.onViewRecycled(holder)
        } catch (e: Exception) {
            Log.e("mException", "ApplyAdapter, onViewRecycled // Exception : ${e.message}")
        }
    }
}