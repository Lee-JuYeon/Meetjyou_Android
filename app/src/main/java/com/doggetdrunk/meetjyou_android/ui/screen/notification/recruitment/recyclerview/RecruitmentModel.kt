package com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview

data class RecruitmentModel(
    val uid : String,
    val userName : String,
    val userSelfieURL : String?,
    val userUID : String,
    val userDescription : String? = null,
    val content : String,
    val partyUID : String
) {
}