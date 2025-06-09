package com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview

import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.NotifyModel


interface IRecruitmentHolderClickListener<RecruitmentModel> {
    fun onHolderClick(model: RecruitmentModel)
    fun onProfileClick(model : RecruitmentModel)
    fun onContentClick(model : RecruitmentModel)
    fun onDenied(model: RecruitmentModel)
    fun onAccept(model : RecruitmentModel)
}