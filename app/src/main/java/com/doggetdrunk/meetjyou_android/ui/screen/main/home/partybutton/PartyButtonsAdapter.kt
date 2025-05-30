package com.doggetdrunk.meetjyou_android.ui.screen.main.home.partybutton

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseDiffUtil
class PartyButtonsAdapter() : RecyclerView.Adapter<PartyButtonsViewHolder>() {

    private val items = ArrayList<PartyButtonModel>()
    fun updateList(newItems : List<PartyButtonModel>?){
        try{
            val diffResult = DiffUtil.calculateDiff(
                object : BaseDiffUtil<PartyButtonModel>(
                    oldList = items,
                    newList = newItems ?: mutableListOf()
                ){},
                false
            )

            diffResult.dispatchUpdatesTo(this)
            items.clear()
            items.addAll(newItems ?: mutableListOf())
        }catch (e:Exception){
            Log.e("mException", "PartyButtonsAdapter, updateList // Exception : ${e.message}")
        }
    }

    private var clickListener: IPartyButtonHolderClickListener<PartyButtonModel>? = null
    fun setOnClickListener(listener: IPartyButtonHolderClickListener<PartyButtonModel>) {
        this.clickListener = listener
    }

    // ViewHolder 재사용을 위한 풀 사이즈 증가
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].uid.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyButtonsViewHolder =
        PartyButtonsViewHolder.create(parent)

    override fun onBindViewHolder(holder: PartyButtonsViewHolder, position: Int) {
        try {
            val item = items.getOrNull(position) ?: return
            val safeClickListener = clickListener ?: object :
                IPartyButtonHolderClickListener<PartyButtonModel> {
                override fun onClick(model: PartyButtonModel) {
                    // 기본 동작 없음
                }
            }
            holder.bind(item, safeClickListener, itemCount = itemCount)
        } catch (e: Exception) {
            Log.e("mException", "PartyButtonsAdapter, onBindViewHolder // Exception : ${e.message}")
        }
    }

    override fun getItemCount() = items.size

    override fun onViewRecycled(holder: PartyButtonsViewHolder) {
        try {
            super.onViewRecycled(holder)
        } catch (e: Exception) {
            Log.e("mException", "PartyButtonsAdapter, onViewRecycled // Exception : ${e.message}")
        }
    }
}
