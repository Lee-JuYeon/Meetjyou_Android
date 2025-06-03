package com.doggetdrunk.meetjyou_android.ui.screen.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
        private const val HOTPOST_ITEM_HEIGHT_DP = 80
        private const val RECOMMENDATION_HEIGHT_DP = 200
        private const val MAX_HOTPOST_ITEMS = 3
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    // ViewPool 재사용을 위한 공유 풀
    private val sharedRecyclerViewPool by lazy { RecyclerView.RecycledViewPool() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 초기화는 한 번만
        if (savedInstanceState == null) {
            initializeUI()
        }
        setupSwipeRefresh()

        observeViewModel()
        loadInitialDataIfNeeded()
    }

    private fun initializeUI() {
        try {
            // RecyclerView들을 병렬로 초기화
            setPartyButtonList(binding.buttonList)
            setHotPostingList(binding.hotpostList)
            setRecommendationList(binding.recommendationList)

            Log.d("HomeFragment", "UI initialization completed")
        } catch (e: Exception) {
            Log.e("mException", "HomeFragment, initializedUI // Exception :${e.localizedMessage}")
        }
    }

    private val partyVM : PartyViewModel by activityViewModels()
    // VM observing FLow로 성능 안정화
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 여러 Flow를 병렬로 수집
                launch {
                    partyVM.hotPostList.collectLatest { list ->
                        if (list.isNotEmpty()) {
                            // 최대 아이템 수 제한으로 성능 향상
                            val limitedList = list.take(MAX_HOTPOST_ITEMS)
                            hotpostListAdapter?.updateList(limitedList)

                            // 동적 높이 설정
                            val density = resources.displayMetrics.density
                            val itemHeight = (HOTPOST_ITEM_HEIGHT_DP * density).toInt()
                            val spacing = (10 * density).toInt()
                            val totalHeight = (limitedList.size * itemHeight) + ((limitedList.size - 1) * spacing)

                            binding.hotpostList.layoutParams = binding.hotpostList.layoutParams.apply {
                                height = totalHeight
                            }

                        } else {
                            Log.w(TAG, "HotPost list is empty")
                        }
                    }
                }

                launch {
                    partyVM.recommendations.collectLatest { list ->
                        if (list.isNotEmpty()) {
                            recommendationAdapter?.updateList(list)
                        } else {
                            Log.e("mException", "Recommendations list is empty")
                        }
                    }
                }

                launch {
                    partyVM.isLoading.collectLatest { isLoading ->
                        Log.d(TAG, "Loading state: $isLoading")
                    }
                }

                launch {
                    partyVM.error.collectLatest { error ->
                        if (error != null) {
                            Log.e("mException", "Error occurred: $error")
                            // TODO: 에러 UI 표시
                        }
                    }
                }
            }
        }
    }

    // 데이터 로드 - 중복 호출 방지
    private fun loadInitialDataIfNeeded() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 병렬로 데이터 로드
                launch { partyVM.loadHotPostList() }
                launch { partyVM.loadRecommendations() }
            } catch (e: Exception) {
                Log.e("mException", "HomeFragment, loadInitialDataIfNeeded // Exception : ${e.localizedMessage}")
            }
        }
    }

    private fun goNotificationActivity(){

    }

    private fun searchTravelSpot(){

    }

    private var partyButtonListAdapter : PartyButtonsAdapter? = null
    private val partyButtonClickListener = object : IPartyButtonHolderClickListener<PartyButtonModel> {
        override fun onClick(model: PartyButtonModel) {
            when (model.uid) {
                "recruitment" -> {
                    // TODO: 파티 모집 화면으로 이동
                }
                "apply" -> {
                    // TODO: 파티 신청 화면으로 이동
                }
            }
        }
    }
    private fun setPartyButtonList(recyclerview : RecyclerView){
        try{
            if (partyButtonListAdapter == null) {
                partyButtonListAdapter = PartyButtonsAdapter().apply {
                    setOnClickListener(partyButtonClickListener)
                }
            }

            recyclerview.apply {
                adapter = partyButtonListAdapter
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                ).apply {
                    initialPrefetchItemCount = 2
                    isItemPrefetchEnabled = true
                }

                setRecycledViewPool(sharedRecyclerViewPool)
                setHasFixedSize(true)
                setItemViewCacheSize(4)

                // 스크롤 최적화
                isNestedScrollingEnabled = false

                // ItemDecoration
                addItemDecoration(RecyclerItemGap(
                    verticalSpace = 0,
                    horizontalSpace = 10,
                    includeEdge = false
                ))
            }

            val mList = listOf<PartyButtonModel>(
                PartyButtonModel(uid = "uid1", imgRes = R.drawable.flag, title = requireContext().getString(R.string.home_party_recruitment_title), description = requireContext().getString(R.string.home_party_recruitment_description)),
                PartyButtonModel(uid = "uid2", imgRes = R.drawable.letter, title = requireContext().getString(R.string.home_party_apply_title), description = requireContext().getString(R.string.home_party_apply_description))
            )

            partyButtonListAdapter?.updateList(mList)
        }catch (e:Exception){
            Log.d("HomeFragment", "HomeFragment, setPartyButtonList, RecyclerView에 데이터 업데이트 완료")
        }
    }


    private var hotpostListAdapter : HotpostAdapter? = null
    private val hotpostClickListener = object : IHotpostHolderClickListener<PartyModel> {
        override fun onClick(model: PartyModel) {
            Log.e("mException", "HomeFragment, 핫포스트 클릭 : ${model.partyTitle}")
        }
    }
    private fun setHotPostingList(recyclerview : RecyclerView){
        try{
            if (hotpostListAdapter == null) {
                hotpostListAdapter = HotpostAdapter().apply {
                    setOnClickListener(hotpostClickListener)
                }
            }

            recyclerview.apply {
                adapter = hotpostListAdapter
                layoutManager = LinearLayoutManager(context).apply {
                    initialPrefetchItemCount = 3
                    isItemPrefetchEnabled = true
                }

                // ViewPool 최적화
                setRecycledViewPool(sharedRecyclerViewPool)
                setHasFixedSize(true)
                setItemViewCacheSize(6)

                // 스크롤 최적화
                isNestedScrollingEnabled = true

                // ItemDecoration
                addItemDecoration(RecyclerItemGap(
                    verticalSpace = 30,
                    horizontalSpace = 0,
                    includeEdge = false
                ))
            }
        }catch (e:Exception){
            Log.e("mException", "HomeFragment, setHotPostingList // Exception : ${e.localizedMessage}")
        }
    }

    private var recommendationAdapter : RecommendationAdapter? = null
    private val recommendationClickListener = object : IRecommendationHolderClickListener<RecommendationModel> {
        override fun onClick(model: RecommendationModel) {
            Log.e("mException", "HomeFragment, 추천지역 아이템 클릭 : ${model.title}")
        }
    }
    private fun setRecommendationList(recyclerview: RecyclerView){
        try{
            if (recommendationAdapter == null) {
                recommendationAdapter = RecommendationAdapter().apply {
                    setOnClickListener(recommendationClickListener)
                }
            }

            recyclerview.apply {
                adapter = recommendationAdapter
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                ).apply {
                    initialPrefetchItemCount = 3
                    isItemPrefetchEnabled = true
                }

                // ViewPool 최적화
                setRecycledViewPool(sharedRecyclerViewPool)
                setHasFixedSize(true)
                setItemViewCacheSize(6)

                // 스크롤 최적화
                isNestedScrollingEnabled = true

                // ItemDecoration
                addItemDecoration(RecyclerItemGap(
                    verticalSpace = 0,
                    horizontalSpace = 30,
                    includeEdge = false
                ))
            }
        }catch (e:Exception){
            Log.d("HomeFragment", "HomeFragment, setRecommendationList, RecyclerView에 데이터 업데이트 완료")
        }
    }

    private fun setupSwipeRefresh() {
        try {
            binding.swipeRefreshLayout.apply {
                // 색상 설정
                setColorSchemeResources(
                    R.color.colorSecondary50,
                    R.color.colorSecondary100,
                    R.color.colorSecondary200
                )

                // 배경색 설정
                setProgressBackgroundColorSchemeResource(R.color.white)

                // 크기 설정 (기본, 큰 크기)
                setSize(androidx.swiperefreshlayout.widget.SwipeRefreshLayout.DEFAULT)

                // 드래그 거리 설정
                setDistanceToTriggerSync(150) // 새로고침 트리거 거리
                setSlingshotDistance(200) // 최대 드래그 거리

                // 새로고침 리스너
                setOnRefreshListener {
                    refreshList()
                }
            }

            Log.d(TAG, "SwipeRefreshLayout setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error in setupSwipeRefresh: ${e.message}", e)
        }
    }

    // hotspot, recoomend list 새로고침
    private var isRefreshing = false  // 새로고침 상태 관리
    private fun refreshList(){
        if (isRefreshing) {
            Log.e("mException", "HomeFragment, refreshList // 이미 리스트 새로고침하여 새로고침 매소드 실행무시하겠음.")
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                isRefreshing = true
                val startTime = System.currentTimeMillis()

                // 강제 새로고침으로 데이터 로드
                val hotPostJob = launch {
                    partyVM.refreshHotPostList(forceRefresh = true)
                }
                val recommendationJob = launch {
                    partyVM.refreshRecommendations(forceRefresh = true)
                }

                // 모든 작업 완료 대기
                hotPostJob.join()
                recommendationJob.join()


            } catch (e: Exception) {
                Log.e(TAG, "Error during refresh: ${e.message}", e)
            } finally {
                isRefreshing = false
                // UI 스레드에서 새로고침 상태 해제
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        try {
            // RecyclerView adapter 해제
            binding.buttonList.adapter = null
            binding.hotpostList.adapter = null
            binding.recommendationList.adapter = null

            // Adapter 참조 해제 - 메모리 정리
            partyButtonListAdapter = null
            hotpostListAdapter = null
            recommendationAdapter = null

            // ViewBinding 정리
            _binding = null

        } catch (e: Exception) {
            Log.e("mException", "HomeFragment, onDestroyView // Exception: ${e.localizedMessage}")
        } finally {
            super.onDestroyView()
        }
    }

    override fun onPause() {
        super.onPause()
        // 필요시 데이터 로딩 일시정지
    }

    override fun onResume() {
        super.onResume()
        // 필요시 데이터 새로고침
    }
}