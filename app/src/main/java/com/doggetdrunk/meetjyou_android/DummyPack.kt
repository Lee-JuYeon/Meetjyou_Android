package com.doggetdrunk.meetjyou_android

import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.model.TravelPlanModel
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.RecommendationModel
import java.util.Date

class DummyPack {

    val dummyRecommendationList = listOf<RecommendationModel>(
        RecommendationModel(uid = "uid1", title = "속초 바다의 설렘", city = "강릉", address = "세경", imagePath = "https://cdn.sisaweek.com/news/photo/202006/135066_125017_5437.jpg"),
        RecommendationModel(uid = "uid2", title = "전주 한옥마을 탐방", city = "전라남도", address = "미오", imagePath = "https://yadolee.com/data/file2/jjalbang/2018/12/9953/thumb-990926193_1545377621.7849_600x0.jpg"),
        RecommendationModel(uid = "uid3", title = "담양 대나무 숲길", city = "충청남도", address = "현진", imagePath = "https://dispatch.cdnser.be/cms-content/uploads/2023/05/22/53581de8-cdd1-455f-8e99-349469c8cc63.jpg"),
        RecommendationModel(uid = "uid3", title = "제주도 감성 여행", city = "제주도", address = "진", imagePath = "https://i.ytimg.com/vi/J13QO-OjOd4/maxresdefault.jpg"),
        RecommendationModel(uid = "uid3", title = "강릉 커피 한잔", city = "강릉", address = "주연", imagePath = "https://i.ytimg.com/vi/lNUWwUMhb5w/maxresdefault.jpg"),
        RecommendationModel(uid = "uid3", title = "여수 밤바다 감성", city = "전라남도", address = "리즈", imagePath = "https://image.news1.kr/system/photos/2024/11/30/7012207/high.jpg"),
        RecommendationModel(uid = "uid3", title = "가평 힐리여행지", city = "경기도", address = "설윤", imagePath = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Sullyoon_%28%EC%84%A4%EC%9C%A4%29_2024_05.jpg/640px-Sullyoon_%28%EC%84%A4%EC%9C%A4%29_2024_05.jpg"),
        RecommendationModel(uid = "uid3", title = "인천 바다 향기", city = "인천", address = "지원", imagePath = "https://culturewindows.com/data/news/2404/9accd0438f409ec4fb79f2d98eabf32a_Ej6SFbQ6h8iRhs7y.jpg")
    )


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