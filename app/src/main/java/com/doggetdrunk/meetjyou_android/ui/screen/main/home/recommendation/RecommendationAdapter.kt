package com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseDiffUtil


class RecommendationAdapter() : RecyclerView.Adapter<RecommendationViewHolder>() {

    private val items = ArrayList<RecommendationModel>()
    fun updateList(newItems : List<RecommendationModel>?){
        try{
            val diffResult = DiffUtil.calculateDiff(
                object : BaseDiffUtil<RecommendationModel>(
                    oldList = items,
                    newList = newItems ?: mutableListOf()
                ){},
                false
            )

            diffResult.dispatchUpdatesTo(this)
            items.clear()
            items.addAll(newItems ?: mutableListOf())
        }catch (e:Exception){
            Log.e("mException", "RecommendationAdapter, updateList // Exception : ${e.message}")
        }
    }

    private var clickListener: IRecommendationHolderClickListener<RecommendationModel>? = null
    fun setOnClickListener(listener: IRecommendationHolderClickListener<RecommendationModel>) {
        this.clickListener = listener
    }

    // ViewHolder 재사용을 위한 풀 사이즈 증가
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].uid.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder =
        RecommendationViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        try {
            val item = items.getOrNull(position) ?: return
            val safeClickListener = clickListener ?: object :
                IRecommendationHolderClickListener<RecommendationModel> {
                override fun onClick(item: RecommendationModel) {
                    // 기본 동작 없음
                }
            }
            holder.bind(item, safeClickListener)
        } catch (e: Exception) {
            Log.e("mException", "RecommendationAdapter, onBindViewHolder // Exception : ${e.message}")
        }
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: RecommendationViewHolder) {
        try {
            super.onViewRecycled(holder)
        } catch (e: Exception) {
            Log.e("mException", "RecommendationAdapter, onViewRecycled // Exception : ${e.message}")
        }
    }
}
