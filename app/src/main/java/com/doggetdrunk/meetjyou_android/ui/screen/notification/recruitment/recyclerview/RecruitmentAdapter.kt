package com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseDiffUtil

class RecruitmentAdapter : RecyclerView.Adapter<RecruitmentViewHolder>() {

    private val items = ArrayList<RecruitmentModel>()

    fun updateList(newItems: List<RecruitmentModel>?) {
        try {
            val diffResult = DiffUtil.calculateDiff(
                object : BaseDiffUtil<RecruitmentModel>(
                    oldList = items,
                    newList = newItems ?: mutableListOf()
                ) {},
                false
            )

            diffResult.dispatchUpdatesTo(this)
            items.clear()
            items.addAll(newItems ?: mutableListOf())
        } catch (e: Exception) {
            Log.e("mException", "RecruitmentAdapter, updateList // Exception : ${e.message}")
        }
    }

    private var clickListener: IRecruitmentHolderClickListener<RecruitmentModel>? = null
    fun setOnClickListener(listener: IRecruitmentHolderClickListener<RecruitmentModel>) {
        this.clickListener = listener
    }

    // ViewHolder 재사용을 위한 풀 사이즈 증가
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].uid.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruitmentViewHolder =
        RecruitmentViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecruitmentViewHolder, position: Int) {
        try {
            val item = items.getOrNull(position) ?: return
            val safeClickListener = clickListener ?: object :
                IRecruitmentHolderClickListener<RecruitmentModel> {
                override fun onHolderClick(model: RecruitmentModel) {
                    // 기본 동작 없음
                }
                override fun onDenied(model: RecruitmentModel) {
                    // 기본 동작 없음
                }
                override fun onAccept(model: RecruitmentModel) {
                    // 기본 동작 없음
                }
            }
            holder.bind(item, safeClickListener)
        } catch (e: Exception) {
            Log.e("mException", "RecruitmentAdapter, onBindViewHolder // Exception : ${e.message}")
        }
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: RecruitmentViewHolder) {
        try {
            super.onViewRecycled(holder)
        } catch (e: Exception) {
            Log.e("mException", "RecruitmentAdapter, onViewRecycled // Exception : ${e.message}")
        }
    }
}