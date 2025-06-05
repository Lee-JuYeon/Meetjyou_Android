package com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview

import java.util.Date

data class ApplyModel (
    val uid : String,
    val partyUID : String,
    val partyThumbNailURL : String?,
    val partyTitle : String,
    val partyContent : String,
    val applyDate : Date,
    val viewCount: Int,
    val returnType : ApplyReturnType
){
}
