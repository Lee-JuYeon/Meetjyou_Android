package com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview

import java.util.Date

data class NotifyModel (
    var uid : String,
    var date : Date,
    var title : String
){
    // 날짜를 문자열로 포맷팅하는 유틸리티 함수
    val formattedDate: String
        get() {
            val formatter = java.text.SimpleDateFormat("yyyy.MM.dd", java.util.Locale.KOREA)
            return formatter.format(date)
        }
}