package com.doggetdrunk.meetjyou_android.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.ApplyRepository
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview.ApplyModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.NotifyModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.NotifyRepository
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.RecruitmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

class NotifyVM : ViewModel() {

    private val notifyRepository: NotifyRepository = NotifyRepository()
    private val recruitmentRepository : RecruitmentRepository = RecruitmentRepository()
    private val applyRepository : ApplyRepository = ApplyRepository()

    private val TAG = "NotificationVM"
    private val EXCEPTION = "mException"

    // Flow 기반 상태 관리
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _notificationList = MutableStateFlow<List<NotifyModel>>(emptyList())
    val notificationList: StateFlow<List<NotifyModel>> = _notificationList.asStateFlow()

    private val _recruitmentList = MutableStateFlow<List<RecruitmentModel>>(emptyList())
    val recruitmentList: StateFlow<List<RecruitmentModel>> = _recruitmentList.asStateFlow()

    private val _applyList = MutableStateFlow<List<ApplyModel>>(emptyList())
    val applyList: StateFlow<List<ApplyModel>> = _applyList.asStateFlow()

    // 캐시 시스템
    private val dataCache = ConcurrentHashMap<String, CacheEntry<*>>()
    private val loadingMutex = Mutex()

    data class CacheEntry<T>(
        val data: T,
        val timestamp: Long
    ) {
        private val CACHE_EXPIRY_MS = 5 * 60 * 1000L // 5분 캐시
        fun isExpired(): Boolean = System.currentTimeMillis() - timestamp > CACHE_EXPIRY_MS
    }

    fun loadApplyList() {
        viewModelScope.launch {
            loadingMutex.withLock {
                if (_isLoading.value) return@withLock

                val cachedData = getCachedData<List<ApplyModel>>("all_applies")
                if (cachedData != null) {
                    Log.d(TAG, "Using cached all applies data")
                    _applyList.value = cachedData
                    return@withLock
                }

                try {
                    _isLoading.value = true
                    Log.d(TAG, "Loading all applies from repository...")

                    val applies = loadWithRetry {
                        // DummyPack에서 직접 가져오기 (실제로는 applyRepository.getApplyList() 같은 메소드 필요)
                        com.doggetdrunk.meetjyou_android.DummyPack().applyList
                    }

                    Log.d(TAG, "All applies loaded: ${applies.size} items")
                    cacheData("all_applies", applies)
                    _applyList.value = applies

                } catch (e: Exception) {
                    Log.e(EXCEPTION, "Failed to load all applies", e)
                    _error.value = "신청 내역을 불러오는데 실패했습니다: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun loadRecruitmentList(partyUID : String) {
        viewModelScope.launch {
            loadingMutex.withLock {
                if (_isLoading.value) return@withLock

                val cachedData = getCachedData<List<RecruitmentModel>>("all_recruitments")
                if (cachedData != null) {
                    Log.d(TAG, "Using cached all recruitments data")
                    _recruitmentList.value = cachedData
                    return@withLock
                }

                try {
                    _isLoading.value = true
                    Log.d(TAG, "Loading all recruitments from repository...")

                    val recruitments = loadWithRetry {
                        recruitmentRepository.getRecruitmentList(partyUID = partyUID)
                    }

                    Log.d(TAG, "All recruitments loaded: ${recruitments.size} items")
                    cacheData("all_recruitments", recruitments)
                    _recruitmentList.value = recruitments

                } catch (e: Exception) {
                    Log.e(EXCEPTION, "Failed to load all recruitments", e)
                    _error.value = "모집 현황을 불러오는데 실패했습니다: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun loadNotificationList() {
        viewModelScope.launch {
            loadingMutex.withLock {
                if (_isLoading.value) return@withLock

                val cachedData = getCachedData<List<NotifyModel>>("all_notifications")
                if (cachedData != null) {
                    Log.d(TAG, "Using cached all notifications data")
                    _notificationList.value = cachedData
                    return@withLock
                }

                try {
                    _isLoading.value = true
                    Log.d(TAG, "Loading all notifications from repository...")

                    val notifications = loadWithRetry {
                        notifyRepository.getNotificationList()
                    }

                    Log.d(TAG, "All notifications loaded: ${notifications.size} items")
                    cacheData("all_notifications", notifications)
                    _notificationList.value = notifications

                } catch (e: Exception) {
                    Log.e(EXCEPTION, "Failed to load all notifications", e)
                    _error.value = "알림을 불러오는데 실패했습니다: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    /**
     * 알림 삭제
     */
    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            try {
                val success = notifyRepository.deleteNotification(notificationId)
                if (success) {
                    // 로컬 상태에서 제거
                    removeLocalNotification(notificationId)
                    Log.d(TAG, "Notification deleted: $notificationId")
                }
            } catch (e: Exception) {
                Log.e(EXCEPTION, "Failed to delete notification", e)
                _error.value = "알림 삭제에 실패했습니다"
            }
        }
    }

    /**
     * 모집 승인/거절 처리
     */
    fun handleRecruitmentAction(model: RecruitmentModel, isAccept: Boolean) {
        viewModelScope.launch {
            try {
                // 실제로는 repository를 통해 서버에 요청
                if (isAccept) {
                    Log.d(TAG, "Recruitment accepted: ${model.uid}")
                    // recruitmentRepository.acceptRecruitment(model.uid)
                } else {
                    Log.d(TAG, "Recruitment denied: ${model.uid}")
                    // recruitmentRepository.deneyRecruitment(model.uid)
                }

                // 로컬 리스트에서 제거
                removeLocalRecruitment(model.uid)

            } catch (e: Exception) {
                Log.e(EXCEPTION, "Failed to handle recruitment action", e)
                _error.value = "요청 처리에 실패했습니다"
            }
        }
    }

    /**
     * 로컬에서 항목 제거
     */
    private fun removeLocalNotification(notificationId: String) {
        _notificationList.value = _notificationList.value.filter { it.uid != notificationId }
    }

    private fun removeLocalRecruitment(recruitmentId: String) {
        _recruitmentList.value = _recruitmentList.value.filter { it.uid != recruitmentId }
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
                kotlinx.coroutines.delay(1000L * (attempt + 1))
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
     * 새로고침
     */
    fun refresh() {
        clearCache()
        loadNotificationList()
        loadRecruitmentList("") // 실제로는 적절한 partyUID 전달
        loadApplyList()
    }

    /**
     * 에러 클리어
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * 캐시 관리
     */
    private fun clearCache() {
        dataCache.clear()
        Log.d(TAG, "NotificationVM, clearCache // Cache cleared")
    }

    /**
     * ViewModel 정리
     */
    override fun onCleared() {
        super.onCleared()
        clearCache()
        Log.d(TAG, "NotificationVM, onCleared // cleared")
    }
}