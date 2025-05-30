package com.doggetdrunk.meetjyou_android.ui.screen.main.home.hotpost

import com.doggetdrunk.meetjyou_android.types.PartyTypes

data class HotpostModel(
    var uid : String,
    var imageURL : String?,
    var title : String,
    var createUserUid : String,
    var createDate : String,
    var viewers : List<String>,
    var isOpen : Boolean,
    var type : PartyTypes
){
    val viewerCountToText: String
        get() = viewers.size.toString()
}

