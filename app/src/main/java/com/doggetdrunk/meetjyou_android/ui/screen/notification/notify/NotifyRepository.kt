package com.doggetdrunk.meetjyou_android.ui.screen.notification.notify

import android.util.Log
import com.doggetdrunk.meetjyou_android.DummyPack
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.NotifyModel
import kotlinx.coroutines.delay

class NotifyRepository() {
    // 모든 알림 가져오기
    suspend fun getNotificationList(): List<NotifyModel> {
        var mList = listOf<NotifyModel>()
        try {
            delay(600) // 네트워크 지연 시뮬레이션
            mList = DummyPack().dummyNotificationList
        } catch (e: Exception) {
            Log.e("mException", "NotificationRepository, getNotificationList // Exception : ${e.localizedMessage}")
            mList = emptyList()
        } finally {
            return mList
        }
    }

    // 알림 삭제
    suspend fun deleteNotification(notificationId: String): Boolean {
        return try {
            delay(300)
            // 실제로는 서버에 삭제 요청
            Log.d("NotificationRepository", "Notification deleted: $notificationId")
            true
        } catch (e: Exception) {
            Log.e("mException", "NotificationRepository, deleteNotification // Exception : ${e.localizedMessage}")
            false
        }
    }

}