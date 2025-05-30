package com.doggetdrunk.meetjyou_android.ui.screen.main.home.hotpost

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseDiffUtil

class HotpostAdapter() : RecyclerView.Adapter<HotpostViewHolder>() {

    private val items = ArrayList<PartyModel>()
    fun updateList(newItems : List<PartyModel>?){
        try{
            val diffResult = DiffUtil.calculateDiff(
                object : BaseDiffUtil<PartyModel>(
                    oldList = items,
                    newList = newItems ?: mutableListOf()
                ){},
                false
            )

            diffResult.dispatchUpdatesTo(this)
            items.clear()
            items.addAll(newItems ?: mutableListOf())
        }catch (e:Exception){
            Log.e("mException", "HotpostAdapter, updateList // Exception : ${e.message}")
        }
    }

    private var clickListener: IHotpostHolderClickListener<PartyModel>? = null
    fun setOnClickListener(listener: IHotpostHolderClickListener<PartyModel>) {
        this.clickListener = listener
    }

    // ViewHolder 재사용을 위한 풀 사이즈 증가
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].uid.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotpostViewHolder =
        HotpostViewHolder.create(parent)

    override fun onBindViewHolder(holder: HotpostViewHolder, position: Int) {
        try {
            val item = items.getOrNull(position) ?: return
            val safeClickListener = clickListener ?: object :
                IHotpostHolderClickListener<PartyModel> {
                override fun onClick(item: PartyModel) {
                    // 기본 동작 없음
                }
            }
            holder.bind(item, safeClickListener)
        } catch (e: Exception) {
            Log.e("mException", "HotpostAdapter, onBindViewHolder // Exception : ${e.message}")
        }
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: HotpostViewHolder) {
        try {
            super.onViewRecycled(holder)
        } catch (e: Exception) {
            Log.e("mException", "HotpostAdapter, onViewRecycled // Exception : ${e.message}")
        }
    }
}
