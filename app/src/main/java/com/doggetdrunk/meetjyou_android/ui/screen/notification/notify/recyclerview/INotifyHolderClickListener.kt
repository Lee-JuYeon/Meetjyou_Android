package com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview

interface INotifyHolderClickListener<NotificationModel> {
    fun onClick(model: NotifyModel)
    fun onDelete(model: NotifyModel)
}