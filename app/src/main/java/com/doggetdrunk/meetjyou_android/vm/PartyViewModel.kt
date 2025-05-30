package com.doggetdrunk.meetjyou_android.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.repository.PartyRepository
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.RecommendationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PartyViewModel(private val partyRepository: PartyRepository = PartyRepository()) : ViewModel() {

    // 로딩 상태
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 에러 상태
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // 파티 리스트
    private val _partyList = MutableLiveData<List<PartyModel>>()
    val partyList: LiveData<List<PartyModel>> = _partyList

    // 핫 포스트 리스트
    private val _hotPostList = MutableLiveData<List<PartyModel>>()
    val hotPostList: LiveData<List<PartyModel>> = _hotPostList

    // 추천 여행지 리스트
    private val _recommendations = MutableLiveData<List<RecommendationModel>>()
    val recommendations: LiveData<List<RecommendationModel>> = _recommendations


    fun loadHotPostList() {
        viewModelScope.launch {
            try {

                val hotPostList = withContext(Dispatchers.IO) {
                    partyRepository.getHotpostList()
                }

                _hotPostList.value = hotPostList

            } catch (e: Exception) {
                Log.e("PartyViewModel", "loadHotPostList error", e)
            }
        }
    }

    fun loadPartyList() {
        viewModelScope.launch {
            try {


                val partyList = withContext(Dispatchers.IO) {
                    partyRepository.getPartyList()
                }

                _partyList.value = partyList

            } catch (e: Exception) {
                Log.e("PartyViewModel", "loadPartyList error", e)
            }
        }
    }

    fun loadRecommendations() {
        viewModelScope.launch {
            try {
                val recommendations = withContext(Dispatchers.IO) {
                    partyRepository.getRecommendations()
                }

                _recommendations.value = recommendations

            } catch (e: Exception) {
                _error.value = "추천 여행지를 불러오는데 실패했습니다: ${e.message}"
                Log.e("PartyViewModel", "loadRecommendations error", e)
            }
        }
    }


    // 에러 메시지 클리어
    fun clearError() {
        _error.value = null
    }
}