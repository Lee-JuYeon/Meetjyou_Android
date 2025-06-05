package com.doggetdrunk.meetjyou_android

import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentModel
import com.doggetdrunk.meetjyou_android.model.TravelPlanModel
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.RecommendationModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview.ApplyModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview.ApplyReturnType
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.NotifyModel
import java.util.Calendar
import java.util.Date

class DummyPack {

    val applyList = listOf(
        ApplyModel(uid = "uid1", partyUID = "uid1", partyThumbNailURL = "https://blog.kakaocdn.net/dn/o1KIw/btqu9mflPY6/rGk1mM3iugV1c6jj9Z3E80/img.jpg", partyTitle = "제주도 여행 멤버 모집! 함게 떠나요!", partyContent = "안녕하세요! \n제주도에서 여유롭고 힐링되는 여행을 함께할 수 있는 사람들을 구하고 있습니다. 많은 신청 부탁드립니다.", applyDate = Calendar.getInstance().apply { add(Calendar.HOUR, -1) }.time, viewCount = 12, returnType = ApplyReturnType.WAIT),
        ApplyModel(uid = "uid2", partyUID = "uid2", partyThumbNailURL = null, partyTitle = "부산 아지메 모임 맴버 구합니다.", partyContent = "안녕하세요! \n제주도에서 여유롭고 힐링되는 여행을 함께할 수 있는 사람들을 구하고 있습니다. 많은 신청 부탁드립니다.", applyDate = Calendar.getInstance().apply { add(Calendar.HOUR, -4) }.time, viewCount = 1200, returnType = ApplyReturnType.ACCEPT),
        ApplyModel(uid = "uid3", partyUID = "uid3", partyThumbNailURL = "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=cee49908-2707-4835-bbcb-cdcba5edf2be", partyTitle = "전주 한옥마을 같이갈 군필3명 선착순모집", partyContent = "안녕하세요! \n제주도에서 여유롭고 힐링되는 여행을 함께할 수 있는 사람들을 구하고 있습니다. 많은 신청 부탁드립니다.", applyDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time, viewCount = 14, returnType = ApplyReturnType.DENIED),
        ApplyModel(uid = "uid4", partyUID = "uid4", partyThumbNailURL = "https://i.namu.wiki/i/YHd-XnjW4JfR-cvDEKafREnwJkEExfb263bG-M8jq1w9118CShun_PY8kyoJ9WuconaviiYV8oPy5zNPnF6Tlw.webp", partyTitle = "혹시 울릉도에 관심있나요? 울릉도 가실분?", partyContent = "안녕하세요! \n제주도에서 여유롭고 힐링되는 여행을 함께할 수 있는 사람들을 구하고 있습니다. 많은 신청 부탁드립니다.", applyDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -5) }.time, viewCount = 690, returnType = ApplyReturnType.WAIT),
        ApplyModel(uid = "uid5", partyUID = "uid5", partyThumbNailURL = "https://image.withstatic.com/271/232/454/e66676171b604d78b686d3036ac63f1b_w1000_h574.jpg?ext=webp", partyTitle = "가평은 역시 서락나인이죠. 선착순2명구해요", partyContent = "안녕하세요! \n제주도에서 여유롭고 힐링되는 여행을 함께할 수 있는 사람들을 구하고 있습니다. 많은 신청 부탁드립니다.", applyDate = Calendar.getInstance().apply { add(Calendar.HOUR, -3) }.time, viewCount = 8730, returnType = ApplyReturnType.WAIT),
        ApplyModel(uid = "uid6", partyUID = "uid6", partyThumbNailURL = "https://www.gptour.go.kr//multi/uploadFile/20240404104322.jpg", partyTitle = "가볍게 놀러갈 가평팸 구해요!", partyContent = "안녕하세요! \n제주도에서 여유롭고 힐링되는 여행을 함께할 수 있는 사람들을 구하고 있습니다. 많은 신청 부탁드립니다.", applyDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -4) }.time, viewCount = 97172, returnType = ApplyReturnType.ACCEPT),
        ApplyModel(uid = "uid7", partyUID = "uid7", partyThumbNailURL = "https://media.triple.guide/triple-cms/c_limit,f_auto,h_2048,w_2048/115defee-c480-48b1-889c-4b990cb1cd9d.jpeg", partyTitle = "경주 한번도 안가본사람 모여라", partyContent = "안녕하세요! \n제주도에서 여유롭고 힐링되는 여행을 함께할 수 있는 사람들을 구하고 있습니다. 많은 신청 부탁드립니다.", applyDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -3) }.time, viewCount = 7, returnType = ApplyReturnType.ACCEPT),
        ApplyModel(uid = "uid8", partyUID = "uid8", partyThumbNailURL = "https://img.ledesk.co.kr/content/2022/20230421182339_pigghmdp.jpg", partyTitle = "당일치기 혜화여행 여성크루", partyContent = "", applyDate = Calendar.getInstance().apply { add(Calendar.HOUR, -2) }.time, viewCount = 152, returnType = ApplyReturnType.DENIED),
        ApplyModel(uid = "uid9", partyUID = "uid9", partyThumbNailURL = null, partyTitle = "", partyContent = "", applyDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) }.time, viewCount = 5234, returnType = ApplyReturnType.DENIED)
    )

    val dummyRecruitmentList = listOf(
        RecruitmentModel(uid = "uid1", userName = "메디슨 비어", userSelfieURL = "https://img.wkorea.com/w/2024/09/style_66d3e11c9cb7a.jpg", userUID = "userUID1", content = "저는 메디슨 비어입니다. 즐거운 파티가 될 수 있도록 성실히 참여하겠습니다. 신중한 신청 검토 부탁드립니다. 감사합니다.", partyUID = "partyUID1"),
        RecruitmentModel(uid = "uid2", userName = "톰 히들스턴", userSelfieURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Tom_Hiddleston_at_the_2024_Toronto_International_Film_Festival_%28cropped%29.jpg/1200px-Tom_Hiddleston_at_the_2024_Toronto_International_Film_Festival_%28cropped%29.jpg", userUID = "userUID2", content = "톰 히들스턴입니다. 잘부탁드립니다.", partyUID = "partyUID2"),
        RecruitmentModel(uid = "uid3", userName = "이마다 미오", userSelfieURL = "https://image.tmdb.org/t/p/original/igOXYhEzSCHU6DGWJv2519BhGaq.jpg", userUID = "userUID3", content = "이마디 미오데스", partyUID = "partyUID3"),
        RecruitmentModel(uid = "uid4", userName = "엘리자베스 올슨", userSelfieURL = "https://cdn.iworldtoday.com/news/photo/201401/28325_11871_4355.jpg", userUID = "userUID4", content = "파티가입부탁드립니다.", partyUID = "partyUID4"),
        RecruitmentModel(uid = "uid5", userName = "이세영", userSelfieURL = "https://pimg.mk.co.kr/meet/neds/2022/01/image_readtop_2022_10768_16413336624906484.jpg", userUID = "userUID5", content = "이세영입니다", partyUID = "partyUID5"),
        RecruitmentModel(uid = "uid6", userName = "신세경", userSelfieURL = "https://dimg.donga.com/wps/SPORTS/IMAGE/2025/05/21/131650601.1.jpg", userUID = "userUID6", content = "신세경입니다. 아무쪼록 잘 부탁드립니다", partyUID = "partyUID6"),
        RecruitmentModel(uid = "uid7", userName = "가나다", userSelfieURL = null, userUID = "userUID7", content = "나는 누구인가, 여긴 어디인가", partyUID = "partyUID7")
    )
    val dummyNotificationList = listOf(
        NotifyModel(
            uid = "noti1",
            date = Calendar.getInstance().apply { add(Calendar.HOUR, -1) }.time,
            title = "새로운 파티 신청이 있습니다"
        ),
        NotifyModel(
            uid = "noti2",
            date = Calendar.getInstance().apply { add(Calendar.HOUR, -3) }.time,
            title = "파티 신청이 승인되었습니다"
        ),
        NotifyModel(
            uid = "noti3",
            date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
            title = "파티 모집이 완료되었습니다"
        ),
        NotifyModel(
            uid = "noti4",
            date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
            title = "새로운 추천 여행지가 업데이트되었습니다"
        ),
        NotifyModel(
            uid = "noti5",
            date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -3) }.time,
            title = "파티 신청이 거절되었습니다"
        ),
        NotifyModel(
            uid = "noti6",
            date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -5) }.time,
            title = "여행 계획서가 업데이트되었습니다"
        ),
        NotifyModel(
            uid = "noti7",
            date = Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, -1) }.time,
            title = "새로운 동행자가 합류했습니다"
        )
    )

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