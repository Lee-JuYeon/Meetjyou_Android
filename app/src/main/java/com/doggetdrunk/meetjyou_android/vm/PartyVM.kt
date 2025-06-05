package com.doggetdrunk.meetjyou_android.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.repository.PartyRepository
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.RecommendationModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

class PartyVM(
    private val partyRepository: PartyRepository = PartyRepository()
) : ViewModel() {

    companion object {
        private const val TAG = "PartyViewModel"
        private const val CACHE_EXPIRY_MS = 5 * 60 * 1000L // 5분 캐시
    }

    // Flow 기반 상태 관리 - 성능 최적화
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 데이터 StateFlow - 백프레셔 및 최신 값 보장
    private val _partyList = MutableStateFlow<List<PartyModel>>(emptyList())
    val partyList: StateFlow<List<PartyModel>> = _partyList.asStateFlow()

    private val _hotPostList = MutableStateFlow<List<PartyModel>>(emptyList())
    val hotPostList: StateFlow<List<PartyModel>> = _hotPostList.asStateFlow()

    private val _recommendations = MutableStateFlow<List<RecommendationModel>>(emptyList())
    val recommendations: StateFlow<List<RecommendationModel>> = _recommendations.asStateFlow()

    // 캐시 시스템 - 메모리 효율성
    private val dataCache = ConcurrentHashMap<String, CacheEntry<*>>()
    private val loadingMutex = Mutex()

    // 로딩 상태 추적
    private val loadingStates = MutableStateFlow(
        LoadingStates(
            hotPostLoading = false,
            recommendationsLoading = false,
            partyListLoading = false
        )
    )

    data class LoadingStates(
        val hotPostLoading: Boolean,
        val recommendationsLoading: Boolean,
        val partyListLoading: Boolean
    ) {
        val isAnyLoading: Boolean
            get() = hotPostLoading || recommendationsLoading || partyListLoading
    }

    data class CacheEntry<T>(
        val data: T,
        val timestamp: Long
    ) {
        fun isExpired(): Boolean = System.currentTimeMillis() - timestamp > CACHE_EXPIRY_MS
    }

    init {
        // 로딩 상태들을 통합하여 전체 로딩 상태 업데이트
        viewModelScope.launch {
            loadingStates.collect { states ->
                _isLoading.value = states.isAnyLoading
            }
        }
    }

    /**
     * HotPost 리스트 로드 - 캐시 및 중복 방지 최적화
     */
    fun loadHotPostList() {
        viewModelScope.launch {
            loadingMutex.withLock {
                // 이미 로딩 중이면 건너뛰기
                if (loadingStates.value.hotPostLoading) {
                    Log.d(TAG, "HotPost already loading, skipping...")
                    return@withLock
                }

                // 캐시 확인
                val cachedData = getCachedData<List<PartyModel>>("hotpost")
                if (cachedData != null) {
                    Log.d(TAG, "Using cached HotPost data")
                    _hotPostList.value = cachedData
                    return@withLock
                }

                try {
                    updateLoadingState { copy(hotPostLoading = true) }
                    Log.d(TAG, "Loading HotPost from repository...")

                    val hotPostList = loadWithRetry {
                        partyRepository.getHotpostList()
                    }

                    Log.d(TAG, "HotPost loaded: ${hotPostList.size} items")

                    // 캐시에 저장
                    cacheData("hotpost", hotPostList)
                    _hotPostList.value = hotPostList

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to load HotPost", e)
                    _error.value = "핫 포스트를 불러오는데 실패했습니다: ${e.message}"
                } finally {
                    updateLoadingState { copy(hotPostLoading = false) }
                }
            }
        }
    }

    /**
     * 파티 리스트 로드 - 최적화
     */
    fun loadPartyList() {
        viewModelScope.launch {
            loadingMutex.withLock {
                if (loadingStates.value.partyListLoading) {
                    Log.d(TAG, "PartyList already loading, skipping...")
                    return@withLock
                }

                val cachedData = getCachedData<List<PartyModel>>("partylist")
                if (cachedData != null) {
                    Log.d(TAG, "Using cached PartyList data")
                    _partyList.value = cachedData
                    return@withLock
                }

                try {
                    updateLoadingState { copy(partyListLoading = true) }
                    Log.d(TAG, "Loading PartyList from repository...")

                    val partyList = loadWithRetry {
                        partyRepository.getPartyList()
                    }

                    Log.d(TAG, "PartyList loaded: ${partyList.size} items")

                    cacheData("partylist", partyList)
                    _partyList.value = partyList

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to load PartyList", e)
                    _error.value = "파티 리스트를 불러오는데 실패했습니다: ${e.message}"
                } finally {
                    updateLoadingState { copy(partyListLoading = false) }
                }
            }
        }
    }

    /**
     * 추천 리스트 로드 - 최적화
     */
    fun loadRecommendations() {
        viewModelScope.launch {
            loadingMutex.withLock {
                if (loadingStates.value.recommendationsLoading) {
                    Log.d(TAG, "Recommendations already loading, skipping...")
                    return@withLock
                }

                val cachedData = getCachedData<List<RecommendationModel>>("recommendations")
                if (cachedData != null) {
                    Log.d(TAG, "Using cached Recommendations data")
                    _recommendations.value = cachedData
                    return@withLock
                }

                try {
                    updateLoadingState { copy(recommendationsLoading = true) }
                    Log.d(TAG, "Loading Recommendations from repository...")

                    val recommendations = loadWithRetry {
                        partyRepository.getRecommendations()
                    }

                    Log.d(TAG, "Recommendations loaded: ${recommendations.size} items")

                    cacheData("recommendations", recommendations)
                    _recommendations.value = recommendations

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to load Recommendations", e)
                    _error.value = "추천 여행지를 불러오는데 실패했습니다: ${e.message}"
                } finally {
                    updateLoadingState { copy(recommendationsLoading = false) }
                }
            }
        }
    }

    /**
     * 재시도 로직이 포함된 로드 함수
     */
    private suspend inline fun <T> loadWithRetry(
        maxRetries: Int = 3,
        crossinline block: suspend () -> T
    ): T {
        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                if (attempt == maxRetries - 1) throw e
                Log.w(TAG, "Load attempt ${attempt + 1} failed, retrying...", e)
                kotlinx.coroutines.delay(1000L * (attempt + 1)) // 지수적 백오프
            }
        }
        error("This should never be reached")
    }

    /**
     * 캐시 관리 함수들
     */
    private fun <T> cacheData(key: String, data: T) {
        dataCache[key] = CacheEntry(data, System.currentTimeMillis())
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getCachedData(key: String): T? {
        val entry = dataCache[key] as? CacheEntry<T>
        return if (entry != null && !entry.isExpired()) {
            entry.data
        } else {
            dataCache.remove(key)
            null
        }
    }

    /**
     * 로딩 상태 업데이트
     */
    private fun updateLoadingState(update: LoadingStates.() -> LoadingStates) {
        loadingStates.value = loadingStates.value.update()
    }

    /**
     * 강제 새로고침 함수들
     */
    fun refreshHotPostList(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            dataCache.remove("hotpost")
        }
        loadHotPostList()
    }

    fun refreshRecommendations(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            dataCache.remove("recommendations")
        }
        loadRecommendations()
    }

    fun refreshPartyList(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            dataCache.remove("partylist")
        }
        loadPartyList()
    }

    /**
     * 전체 새로고침
     */
    fun refreshAll(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (forceRefresh) {
                clearCache()
            }

            // 병렬로 모든 데이터 로드
            launch { loadHotPostList() }
            launch { loadRecommendations() }
            launch { loadPartyList() }
        }
    }

    /**
     * 캐시 관리
     */
    fun clearCache() {
        dataCache.clear()
        Log.d(TAG, "Cache cleared")
    }

    fun clearExpiredCache() {
        val keysToRemove = dataCache.keys.filter { key ->
            (dataCache[key] as? CacheEntry<*>)?.isExpired() == true
        }
        keysToRemove.forEach { dataCache.remove(it) }
        Log.d(TAG, "Expired cache cleared: ${keysToRemove.size} entries")
    }

    /**
     * 에러 관리
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * ViewModel 정리
     */
    override fun onCleared() {
        super.onCleared()
        clearCache()
        Log.d(TAG, "ViewModel cleared")
    }
}