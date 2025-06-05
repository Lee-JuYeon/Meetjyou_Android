package com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview

import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.NotifyModel


interface IRecruitmentHolderClickListener<RecruitmentModel> {
    fun onHolderClick(model: RecruitmentModel)
    fun onDenied(model: RecruitmentModel)
    fun onAccept(model : RecruitmentModel)
}