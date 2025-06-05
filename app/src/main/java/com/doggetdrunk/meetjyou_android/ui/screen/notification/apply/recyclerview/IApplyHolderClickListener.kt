package com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview

interface IApplyHolderClickListener<ApplyModel> {
    fun onPostClick(model: ApplyModel)
    fun onButtonClicm(type: ApplyReturnType)
}