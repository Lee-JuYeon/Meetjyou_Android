package com.doggetdrunk.meetjyou_android.ui.screen.notification.apply

import android.util.Log
import com.doggetdrunk.meetjyou_android.DummyPack
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentModel
import kotlinx.coroutines.delay

class ApplyRepository  {

    // 내 파티에 가입신청한 '모집 관리' 리스트
    suspend fun getApplyList(partyUID : String) : List<RecruitmentModel>{
        // party uid를 비교
        return try {
            delay(300)
            DummyPack().dummyRecruitmentList
        }catch (e: Exception) {
            Log.e("mException", "NotificationRepository, getRecruitmentList // Exception : ${e.localizedMessage}")
            listOf()
        }

    }

    suspend fun getCurrrentApplyModel(){

    }

    suspend fun deneyApply(){

    }

    suspend fun acceptApply(){

    }


}