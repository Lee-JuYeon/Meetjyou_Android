package com.doggetdrunk.meetjyou_android.model

import com.doggetdrunk.meetjyou_android.types.LifeStyleType
import java.util.Date

data class PartyModel(
    var uid : String,
    var partyTitle : String, // 파티 이름
    var partyDescription : String, // 파티 정보
    var isOpen : Boolean, // 모집중, 모집완료
    var isThundering : Boolean, // 번개인가?
    var address : String, // 파티 여행지역
    var createDate : Date, // 파티 생성일자
    var createUserUid : String, // 파티 생성자
    var imageURL : String, // 파티 이미지
    var viewers : List<String>, // 조회수
    var requestPartyList : List<RequestPartyModel>, // 파티 신청 리스트
    var partyMembersUID : List<String>, // 파티 맴버
    var maxPartyMember : Int, // 파티 최대 인원
    var travelStartDate : Date, // 동행 시작 일자
    var travelEndDate : Date, // 동행 종료 일자
    var lifeStyleTypeList : List<LifeStyleType>, // 성격 chip 리스트
    var travelPlan : TravelPlanModel // 여행계획서
){
    val viewerCountToText: String
        get() = viewers.size.toString()
}



