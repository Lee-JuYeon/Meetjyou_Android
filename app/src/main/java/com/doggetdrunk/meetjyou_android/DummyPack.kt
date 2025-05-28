package com.doggetdrunk.meetjyou_android

import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.model.TravelPlanModel
import java.util.Date

class DummyPack {

    val dummyPostList = listOf<PartyModel>(
        PartyModel(
            uid = "uid1",
            partyTitle = "제주도 여행 맴버 모집! 함께 떠나요",
            partyDescription = "제주도 내용 1",
            isOpen = true,
            isThundering = true,
            address = "제주도 서귀포시",
            createDate = Date(),
            createUserUid = "userUID1",
            imageURL = "https://blog.kakaocdn.net/dn/o1KIw/btqu9mflPY6/rGk1mM3iugV1c6jj9Z3E80/img.jpg",
            viewers = listOf<String>(),
            requestPartyList = listOf(),
            partyMembersUID = listOf(),
            maxPartyMember = 14,
            travelStartDate = Date(),
            travelEndDate = Date(),
            lifeStyleTypeList = listOf(),
            travelPlan = TravelPlanModel(uid = "uid1")
        ),
        PartyModel(
            uid = "uid2",
            partyTitle = "홍대에서 잠깐 벙개하실분",
            partyDescription = "홍대 내용 1",
            isOpen = false,
            isThundering = false,
            address = "서울특별시 금정구",
            createDate = Date(),
            createUserUid = "userUID2",
            imageURL = "https://blog.kakaocdn.net/dn/MsXQm/btsIQxgm3ko/KNwd5hUzJmPyqkO2gB2Iak/img.jpg",
            viewers = listOf<String>(""),
            requestPartyList = listOf(),
            partyMembersUID = listOf(),
            maxPartyMember = 20,
            travelStartDate = Date(),
            travelEndDate = Date(),
            lifeStyleTypeList = listOf(),
            travelPlan = TravelPlanModel(uid = "uid1")
        ),
        PartyModel(
            uid = "uid3",
            partyTitle = "부산 여행, 동행자 구해요~~",
            partyDescription = "부산 내용 1",
            isOpen = true,
            isThundering = false,
            address = "부산광역시 금정구",
            createDate = Date(),
            createUserUid = "userUID3",
            imageURL = "https://econmingle.com/wp-content/uploads/2025/02/Concerns-over-Busans-extinction-6-scaled.jpg",
            viewers = listOf<String>("","","","","",""),
            requestPartyList = listOf(),
            partyMembersUID = listOf(),
            maxPartyMember = 5,
            travelStartDate = Date(),
            travelEndDate = Date(),
            lifeStyleTypeList = listOf(),
            travelPlan = TravelPlanModel(uid = "uid1")
        )
    )
}