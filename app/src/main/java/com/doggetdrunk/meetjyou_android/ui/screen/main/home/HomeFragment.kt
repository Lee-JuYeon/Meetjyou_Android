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


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val partyVM : PartyViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPartyButtonList(recyclerview = binding.buttonList)
        setHotPostingList(recyclerview = binding.hotpostList)
        setRecommendationList(recyclerview = binding.recommendationList)

        // Observer를 먼저 설정
        setVM()

        // 그 다음에 데이터 로드
        loadData()
    }

    private fun setVM(){
        try{
            // Observer들을 먼저 설정
            partyVM.hotPostList.observe(viewLifecycleOwner){ list : List<PartyModel> ->
                Log.d("HomeFragment", "HotPost list updated: ${list.size} items")
                hotpostListAdapter?.updateList(list)
            }

            partyVM.recommendations.observe(viewLifecycleOwner){ list : List<RecommendationModel> ->
                Log.d("HomeFragment", "Recommendations updated: ${list.size} items")
                recommendationAdapter?.updateList(list)
            }

        }catch (e:Exception){
            Log.e("mException", "HomeFragment, setVM // Exception : ${e.localizedMessage}")
        }
    }

    private fun loadData(){
        try{
            // Observer 설정 후 데이터 로드
            partyVM.loadPartyList()
            partyVM.loadHotPostList()
            partyVM.loadRecommendations()
        }catch (e:Exception){
            Log.e("mException", "HomeFragment, loadData // Exception : ${e.localizedMessage}")
        }
    }

    private fun goNotificationActivity(){

    }

    private fun searchTravelSpot(){

    }

    private var partyButtonListAdapter : PartyButtonsAdapter? = null
    private fun setPartyButtonList(recyclerview : RecyclerView){
        if (partyButtonListAdapter == null){
            partyButtonListAdapter = PartyButtonsAdapter()
        }

        recyclerview.apply {
            adapter = partyButtonListAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                initialPrefetchItemCount = 2
            }
            recycledViewPool.setMaxRecycledViews(0,2)
            addItemDecoration(RecyclerItemGap(verticalSpace = 0, horizontalSpace = 10, includeEdge = false))
        }

        val mList = listOf<PartyButtonModel>(
            PartyButtonModel(uid = "uid1", imgRes = R.drawable.flag, title = requireContext().getString(R.string.home_party_recruitment_title), description = requireContext().getString(R.string.home_party_recruitment_description)),
            PartyButtonModel(uid = "uid2", imgRes = R.drawable.letter, title = requireContext().getString(R.string.home_party_apply_title), description = requireContext().getString(R.string.home_party_apply_description))
        )

        try{
            partyButtonListAdapter?.updateList(mList)
            partyButtonListAdapter?.setOnClickListener(object :
                IPartyButtonHolderClickListener<PartyButtonModel> {
                override fun onClick(model: PartyButtonModel) {
                    when(model.title){
                        "파티 모집" -> {

                        }
                        "파티 신청" -> {

                        }
                    }
                }
            })
        }catch (e:Exception){
            Log.d("HomeFragment", "HomeFragment, setPartyButtonList, RecyclerView에 데이터 업데이트 완료")
        }
    }


    private var hotpostListAdapter : HotpostAdapter? = null
    private fun setHotPostingList(recyclerview : RecyclerView){
        if (hotpostListAdapter == null){
            hotpostListAdapter = HotpostAdapter()
        }

        recyclerview.apply {
            adapter = hotpostListAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
                initialPrefetchItemCount = 3
            }
            recycledViewPool.setMaxRecycledViews(0,3)
            addItemDecoration(RecyclerItemGap(verticalSpace = 10, horizontalSpace = 0, includeEdge = false))

            // HotPost 리스트 독립 스크롤 활성화
            isNestedScrollingEnabled = true
        }


        try{
            hotpostListAdapter?.setOnClickListener(object :
                IHotpostHolderClickListener<PartyModel> {
                override fun onClick(model: PartyModel) {
                    Log.d("HomeFragment", "HomeFragment, setHotPostingList, ${model.partyTitle}이 클릭됨")
                }
            })
        }catch (e:Exception){
            Log.e("mException", "HomeFragment, setHotPostingList // Exception : ${e.localizedMessage}")
        }
    }

    private var recommendationAdapter : RecommendationAdapter? = null
    private fun setRecommendationList(recyclerview: RecyclerView){
        if (recommendationAdapter == null){
            recommendationAdapter = RecommendationAdapter()
        }

        recyclerview.apply {
            adapter = recommendationAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                initialPrefetchItemCount = 3
            }
            recycledViewPool.setMaxRecycledViews(0,3)
            addItemDecoration(RecyclerItemGap(verticalSpace = 0, horizontalSpace = 10, includeEdge = false))
            isNestedScrollingEnabled = true // Recommendation 리스트 독립 스크롤 활성화
        }


        try{
            recommendationAdapter?.setOnClickListener(object :
                IRecommendationHolderClickListener<RecommendationModel> {
                override fun onClick(model: RecommendationModel) {
                    Log.d("HomeFragment", "HomeFragment, setRecommendationList, ${model.title}이 클릭됨")
                }
            })
        }catch (e:Exception){
            Log.d("HomeFragment", "HomeFragment, setRecommendationList, RecyclerView에 데이터 업데이트 완료")
        }
    }

}