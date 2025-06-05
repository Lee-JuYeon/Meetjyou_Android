package com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseDiffUtil


class NotifyAdapter : RecyclerView.Adapter<NotifyViewHolder>() {

    private val items = ArrayList<NotifyModel>()

    fun updateList(newItems: List<NotifyModel>?) {
        try {
            val diffResult = DiffUtil.calculateDiff(
                object : BaseDiffUtil<NotifyModel>(
                    oldList = items,
                    newList = newItems ?: mutableListOf()
                ) {},
                false
            )

            diffResult.dispatchUpdatesTo(this)
            items.clear()
            items.addAll(newItems ?: mutableListOf())
        } catch (e: Exception) {
            Log.e("mException", "NotificationAdapter, updateList // Exception : ${e.message}")
        }
    }

    private var clickListener: INotifyHolderClickListener<NotifyModel>? = null
    fun setOnClickListener(listener: INotifyHolderClickListener<NotifyModel>) {
        this.clickListener = listener
    }

    // ViewHolder 재사용을 위한 풀 사이즈 증가
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].uid.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder =
        NotifyViewHolder.create(parent)

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        try {
            val item = items.getOrNull(position) ?: return
            val safeClickListener = clickListener ?: object :
                INotifyHolderClickListener<NotifyModel> {
                override fun onClick(model: NotifyModel) {
                    // 기본 동작 없음
                }
                override fun onDelete(model: NotifyModel) {
                    // 기본 동작 없음
                }
            }
            holder.bind(item, safeClickListener)
        } catch (e: Exception) {
            Log.e("mException", "NotificationAdapter, onBindViewHolder // Exception : ${e.message}")
        }
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: NotifyViewHolder) {
        try {
            super.onViewRecycled(holder)
        } catch (e: Exception) {
            Log.e("mException", "NotificationAdapter, onViewRecycled // Exception : ${e.message}")
        }
    }
}