package com.doggetdrunk.meetjyou_android.ui.screen.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.FragmentHomeBinding
import com.doggetdrunk.meetjyou_android.model.PartyModel
import com.doggetdrunk.meetjyou_android.ui.custom.recyclerview.RecyclerItemGap
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.hotpost.HotpostAdapter
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.hotpost.IHotpostHolderClickListener
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.partybutton.IPartyButtonHolderClickListener
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.partybutton.PartyButtonModel
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.partybutton.PartyButtonsAdapter
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.IRecommendationHolderClickListener
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.RecommendationAdapter
import com.doggetdrunk.meetjyou_android.ui.screen.main.home.recommendation.RecommendationModel
import com.doggetdrunk.meetjyou_android.vm.PartyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
        private const val HOTPOST_ITEM_HEIGHT_DP = 80
        private const val MAX_HOTPOST_ITEMS = 3
    }

    // ViewBinding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val partyVM: PartyViewModel by activityViewModels()

    // Adapters
    private var partyButtonsAdapter: PartyButtonsAdapter? = null
    private var hotpostAdapter: HotpostAdapter? = null
    private var recommendationAdapter: RecommendationAdapter? = null

    // RecyclerView 성능 최적화를 위한 공유 ViewPool
    private val sharedRecyclerViewPool by lazy { RecyclerView.RecycledViewPool() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerViews()
        observeViewModel()
        loadData()
    }

    /**
     * UI 초기 설정
     */
    private fun setupUI() {
        try {
            // 상단 버튼들 클릭 리스너
            binding.logo.setOnClickListener {
                // TODO: 로고 클릭 처리
                Log.d(TAG, "Logo clicked")
            }

            binding.notificationBtn.setOnClickListener {
                // TODO: 알림 화면으로 이동
                goToNotificationActivity()
            }

            // 검색창 클릭 리스너
            binding.search.setOnClickListener {
                // TODO: 검색 화면으로 이동
                searchTravelSpot()
            }

            // SwipeRefreshLayout 설정
            binding.swipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in setupUI: ${e.message}", e)
        }
    }

    /**
     * RecyclerView들 설정
     */
    private fun setupRecyclerViews() {
        try {
            setupPartyButtonsList()
            setupHotPostList()
            setupRecommendationList()

            Log.d(TAG, "All RecyclerViews setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error in setupRecyclerViews: ${e.message}", e)
        }
    }

    /**
     * 파티 버튼 리스트 설정
     */
    private fun setupPartyButtonsList() {
        // Adapter 초기화
        partyButtonsAdapter = PartyButtonsAdapter().apply {
            setOnClickListener(object : IPartyButtonHolderClickListener<PartyButtonModel> {
                override fun onClick(model: PartyButtonModel) {
                    handlePartyButtonClick(model)
                }
            })
        }

        // RecyclerView 설정
        binding.buttonList.apply {
            adapter = partyButtonsAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            ).apply {
                initialPrefetchItemCount = 2
                isItemPrefetchEnabled = true
            }

            // 성능 최적화 설정
            setRecycledViewPool(sharedRecyclerViewPool)
            setHasFixedSize(true)
            setItemViewCacheSize(4)
            isNestedScrollingEnabled = false

            // 아이템 간격 설정
            addItemDecoration(RecyclerItemGap(
                verticalSpace = 0,
                horizontalSpace = 10,
                includeEdge = false
            ))
        }

        // 초기 데이터 설정
        val buttonList = createPartyButtonList()
        partyButtonsAdapter?.updateList(buttonList)
    }

    /**
     * 핫포스트 리스트 설정
     */
    private fun setupHotPostList() {
        // Adapter 초기화
        hotpostAdapter = HotpostAdapter().apply {
            setOnClickListener(object : IHotpostHolderClickListener<PartyModel> {
                override fun onClick(model: PartyModel) {
                    handleHotpostClick(model)
                }
            })
        }

        // RecyclerView 설정
        binding.hotpostList.apply {
            adapter = hotpostAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                initialPrefetchItemCount = 3
                isItemPrefetchEnabled = true
            }

            // 성능 최적화 설정
            setRecycledViewPool(sharedRecyclerViewPool)
            setHasFixedSize(true)
            setItemViewCacheSize(6)
            isNestedScrollingEnabled = true

            // 아이템 간격 설정
            addItemDecoration(RecyclerItemGap(
                verticalSpace = 10,
                horizontalSpace = 0,
                includeEdge = false
            ))
        }
    }

    /**
     * 추천 리스트 설정
     */
    private fun setupRecommendationList() {
        // Adapter 초기화
        recommendationAdapter = RecommendationAdapter().apply {
            setOnClickListener(object : IRecommendationHolderClickListener<RecommendationModel> {
                override fun onClick(model: RecommendationModel) {
                    handleRecommendationClick(model)
                }
            })
        }

        // RecyclerView 설정
        binding.recommendationList.apply {
            adapter = recommendationAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            ).apply {
                initialPrefetchItemCount = 3
                isItemPrefetchEnabled = true
            }

            // 성능 최적화 설정
            setRecycledViewPool(sharedRecyclerViewPool)
            setHasFixedSize(true)
            setItemViewCacheSize(6)
            isNestedScrollingEnabled = true

            // 아이템 간격 설정
            addItemDecoration(RecyclerItemGap(
                verticalSpace = 0,
                horizontalSpace = 10,
                includeEdge = false
            ))
        }
    }

    /**
     * ViewModel 옵저빙
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 핫포스트 리스트 관찰
                launch {
                    partyVM.hotPostList.collectLatest { list ->
                        handleHotPostListUpdate(list)
                    }
                }

                // 추천 리스트 관찰
                launch {
                    partyVM.recommendations.collectLatest { list ->
                        handleRecommendationListUpdate(list)
                    }
                }

                // 로딩 상태 관찰
                launch {
                    partyVM.isLoading.collectLatest { isLoading ->
                        handleLoadingStateUpdate(isLoading)
                    }
                }

                // 에러 상태 관찰
                launch {
                    partyVM.error.collectLatest { error ->
                        handleErrorUpdate(error)
                    }
                }
            }
        }
    }

    /**
     * 초기 데이터 로드
     */
    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 병렬로 데이터 로드
                launch { partyVM.loadHotPostList() }
                launch { partyVM.loadRecommendations() }

                Log.d(TAG, "Initial data loading started")
            } catch (e: Exception) {
                Log.e(TAG, "Error in loadData: ${e.message}", e)
            }
        }
    }

    /**
     * 데이터 새로고침
     */
    private fun refreshData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 강제 새로고침
                launch { partyVM.refreshHotPostList(forceRefresh = true) }
                launch { partyVM.refreshRecommendations(forceRefresh = true) }

                Log.d(TAG, "Data refresh started")
            } catch (e: Exception) {
                Log.e(TAG, "Error in refreshData: ${e.message}", e)
            } finally {
                // SwipeRefreshLayout 종료
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    /**
     * 파티 버튼 리스트 생성
     */
    private fun createPartyButtonList(): List<PartyButtonModel> {
        return listOf(
            PartyButtonModel(
                uid = "recruitment",
                imgRes = R.drawable.flag,
                title = getString(R.string.home_party_recruitment_title),
                description = getString(R.string.home_party_recruitment_description)
            ),
            PartyButtonModel(
                uid = "apply",
                imgRes = R.drawable.letter,
                title = getString(R.string.home_party_apply_title),
                description = getString(R.string.home_party_apply_description)
            )
        )
    }

    /**
     * 핫포스트 리스트 업데이트 처리
     */
    private fun handleHotPostListUpdate(list: List<PartyModel>) {
        try {
            if (list.isNotEmpty()) {
                // 최대 아이템 수 제한
                val limitedList = list.take(MAX_HOTPOST_ITEMS)
                hotpostAdapter?.updateList(limitedList)

                // 동적 높이 설정
                updateHotPostListHeight(limitedList.size)

                Log.d(TAG, "HotPost list updated: ${limitedList.size} items")
            } else {
                Log.w(TAG, "HotPost list is empty")
                hotpostAdapter?.updateList(emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleHotPostListUpdate: ${e.message}", e)
        }
    }

    /**
     * 추천 리스트 업데이트 처리
     */
    private fun handleRecommendationListUpdate(list: List<RecommendationModel>) {
        try {
            if (list.isNotEmpty()) {
                recommendationAdapter?.updateList(list)
                Log.d(TAG, "Recommendation list updated: ${list.size} items")
            } else {
                Log.w(TAG, "Recommendation list is empty")
                recommendationAdapter?.updateList(emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleRecommendationListUpdate: ${e.message}", e)
        }
    }

    /**
     * 로딩 상태 업데이트 처리
     */
    private fun handleLoadingStateUpdate(isLoading: Boolean) {
        try {
            // SwipeRefreshLayout에 로딩 상태 반영
            if (!binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = isLoading
            }

            Log.d(TAG, "Loading state updated: $isLoading")
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleLoadingStateUpdate: ${e.message}", e)
        }
    }

    /**
     * 에러 업데이트 처리
     */
    private fun handleErrorUpdate(error: String?) {
        try {
            if (error != null) {
                Log.e(TAG, "Error occurred: $error")
                // TODO: 사용자에게 에러 메시지 표시

                // 에러 발생 시 새로고침 중단
                binding.swipeRefreshLayout.isRefreshing = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleErrorUpdate: ${e.message}", e)
        }
    }

    /**
     * 핫포스트 리스트 높이 업데이트
     */
    private fun updateHotPostListHeight(itemCount: Int) {
        try {
            val density = resources.displayMetrics.density
            val itemHeight = (HOTPOST_ITEM_HEIGHT_DP * density).toInt()
            val spacing = (10 * density).toInt()
            val totalHeight = (itemCount * itemHeight) + ((itemCount - 1) * spacing)

            binding.hotpostList.layoutParams = binding.hotpostList.layoutParams.apply {
                height = totalHeight
            }

            Log.d(TAG, "HotPost list height updated: $totalHeight px for $itemCount items")
        } catch (e: Exception) {
            Log.e(TAG, "Error in updateHotPostListHeight: ${e.message}", e)
        }
    }

    /**
     * 클릭 이벤트 처리 메서드들
     */
    private fun handlePartyButtonClick(model: PartyButtonModel) {
        try {
            when (model.uid) {
                "recruitment" -> {
                    Log.d(TAG, "Party recruitment clicked")
                    // TODO: 파티 모집 화면으로 이동
                }
                "apply" -> {
                    Log.d(TAG, "Party apply clicked")
                    // TODO: 파티 신청 화면으로 이동
                }
                else -> {
                    Log.w(TAG, "Unknown party button clicked: ${model.uid}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in handlePartyButtonClick: ${e.message}", e)
        }
    }

    private fun handleHotpostClick(model: PartyModel) {
        try {
            Log.d(TAG, "Hotpost clicked: ${model.partyTitle}")
            // TODO: 파티 상세 화면으로 이동
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleHotpostClick: ${e.message}", e)
        }
    }

    private fun handleRecommendationClick(model: RecommendationModel) {
        try {
            Log.d(TAG, "Recommendation clicked: ${model.title}")
            // TODO: 추천 여행지 상세 화면으로 이동
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleRecommendationClick: ${e.message}", e)
        }
    }

    private fun goToNotificationActivity() {
        try {
            Log.d(TAG, "Going to notification activity")
            // TODO: 알림 화면으로 이동 구현
        } catch (e: Exception) {
            Log.e(TAG, "Error in goToNotificationActivity: ${e.message}", e)
        }
    }

    private fun searchTravelSpot() {
        try {
            Log.d(TAG, "Search travel spot clicked")
            // TODO: 검색 화면으로 이동 구현
        } catch (e: Exception) {
            Log.e(TAG, "Error in searchTravelSpot: ${e.message}", e)
        }
    }

    /**
     * 메모리 정리
     */
    override fun onDestroyView() {
        try {
            // RecyclerView adapter 해제
            binding.buttonList.adapter = null
            binding.hotpostList.adapter = null
            binding.recommendationList.adapter = null

            // Adapter 참조 해제
            partyButtonsAdapter = null
            hotpostAdapter = null
            recommendationAdapter = null

            Log.d(TAG, "Memory cleanup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onDestroyView: ${e.message}", e)
        } finally {
            // ViewBinding 정리
            _binding = null
            super.onDestroyView()
        }
    }

    /**
     * 라이프사이클 관리
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Fragment resumed")
        // 필요시 데이터 새로고침
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Fragment paused")
        // 필요시 로딩 중단
    }
}