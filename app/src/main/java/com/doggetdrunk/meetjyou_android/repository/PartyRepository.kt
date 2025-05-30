package com.doggetdrunk.meetjyou_android.repository

import android.util.Log
import com.doggetdrunk.meetjyou_android.DummyPack
import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.RecommendationModel
import kotlinx.coroutines.delay

class PartyRepository() {

    // 모든 파티 보기
    suspend fun getPartyList(): List<PartyModel> {
        var mList = listOf<PartyModel>()
        try{
            delay(600)
            mList = DummyPack().dummyPostList
        }catch (e: Exception) {
            Log.e("mException", "PartyRepository, getPartyList // Exception : ${e.localizedMessage}")
            mList = emptyList()
        }finally{
            return mList
        }
    }

    // 핫한 파티 보기
    suspend fun getHotpostList(): List<PartyModel> {
        var mList = listOf<PartyModel>()
        try{
            delay(600)
            mList = DummyPack().dummyPostList
        }catch (e: Exception) {
            Log.e("mException", "PartyRepository, getHotpostList // Exception : ${e.localizedMessage}")
            mList = emptyList()
        }finally{
            return mList
        }
    }

    // 추천 여행지 목록
    suspend fun getRecommendations(): List<RecommendationModel> {
        var mList = listOf<RecommendationModel>()
        try{
            delay(600)
            mList = DummyPack().dummyRecommendationList
        }catch (e: Exception) {
            Log.e("mException", "PartyRepository, getRecommendations // Exception : ${e.localizedMessage}")
            mList = emptyList()
        }finally{
             return mList
        }
    }

    // 파티 신청 리스트 모두보기
    suspend fun getRequestPartList(): List<PartyModel> {
        return try {
            delay(500)
            // 실제 구현 필요
            emptyList()
        } catch (e: Exception) {
            Log.e("PartyRepository", "getRequestPartList error", e)
            emptyList()
        }
    }

    // 파티 신청 보내기
    suspend fun sendRequestPart(partyId: String): Boolean {
        return try {
            delay(1000)
            // 실제 API 호출
            true
        } catch (e: Exception) {
            Log.e("PartyRepository", "sendRequestPart error", e)
            false
        }
    }

    // 위치 추천
    suspend fun recommendLocation(): List<LocationModel> {
        return try {
            delay(300)
            // 실제 구현 필요
            emptyList()
        } catch (e: Exception) {
            Log.e("PartyRepository", "recommendLocation error", e)
            emptyList()
        }
    }
}

data class LocationModel(
    var uid : String,
    var title : String,
    var address : String
){

}


