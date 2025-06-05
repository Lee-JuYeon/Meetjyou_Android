package com.doggetdrunk.meetjyou_android.ui.screen.notification.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doggetdrunk.meetjyou_android.databinding.FragmentNotificationRecruitmentStatusBinding
import com.doggetdrunk.meetjyou_android.ui.custom.recyclerview.RecyclerItemGap
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.IRecruitmentHolderClickListener
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentAdapter
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentModel
import com.doggetdrunk.meetjyou_android.vm.NotifyVM
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationRecruitmentStatusFragment : Fragment() {

    private var _binding: FragmentNotificationRecruitmentStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNotificationRecruitmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView가 있다면 설정 (현재 레이아웃에는 없으므로 가상의 recyclerview가 있다고 가정)
        setRecruitmentList()
        observingData()
        loadInitialDataIfNeeded()
    }

    private val notifyVM: NotifyVM by activityViewModels()

    private fun observingData() {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        notifyVM.recruitmentList.collectLatest { list ->
                            if (list.isNotEmpty()) {
                                recruitmentAdapter?.updateList(list)
                                Log.d("RecruitmentFragment", "Recruitment list updated: ${list.size} items")
                            } else {
                                Log.d("RecruitmentFragment", "Recruitment list is empty")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("mException", "NotificationRecruitmentStatusFragment, observingData // Exception : ${e.localizedMessage}")
        }
    }

    private fun loadInitialDataIfNeeded() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 실제로는 현재 사용자의 파티 UID를 전달해야 함
                launch { notifyVM.loadRecruitmentList("") }
            } catch (e: Exception) {
                Log.e("mException", "NotificationRecruitmentStatusFragment, loadInitialDataIfNeeded // Exception : ${e.localizedMessage}")
            }
        }
    }

    private var recruitmentAdapter: RecruitmentAdapter? = null
    private val recruitmentClickListener = object : IRecruitmentHolderClickListener<RecruitmentModel> {
        override fun onHolderClick(model: RecruitmentModel) {
            Log.d("RecruitmentFragment", "Recruitment item clicked: ${model.userName}")
            // TODO: 상세보기 화면으로 이동
        }

        override fun onDenied(model: RecruitmentModel) {
            Log.d("RecruitmentFragment", "Recruitment denied: ${model.userName}")
            notifyVM.handleRecruitmentAction(model, false)
        }

        override fun onAccept(model: RecruitmentModel) {
            Log.d("RecruitmentFragment", "Recruitment accepted: ${model.userName}")
            notifyVM.handleRecruitmentAction(model, true)
        }
    }

    private val sharedRecyclerViewPool by lazy { RecyclerView.RecycledViewPool() }

    private fun setRecruitmentList() {
        try {
            if (recruitmentAdapter == null) {
                recruitmentAdapter = RecruitmentAdapter().apply {
                    setOnClickListener(recruitmentClickListener)
                }
            }

            binding.recyclerview.apply {
                adapter = recruitmentAdapter
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                ).apply {
                    initialPrefetchItemCount = 5
                    isItemPrefetchEnabled = true
                }

                setRecycledViewPool(sharedRecyclerViewPool)
                setHasFixedSize(true)
                setItemViewCacheSize(4)

                // ItemDecoration
                addItemDecoration(
                    RecyclerItemGap(
                        verticalSpace = 20,
                        horizontalSpace = 0,
                        includeEdge = false
                    )
                )
            }

            Log.d("RecruitmentFragment", "Recruitment RecyclerView setup completed")
        } catch (e: Exception) {
            Log.e("mException", "NotificationRecruitmentStatusFragment, setRecruitmentList // Exception : ${e.localizedMessage}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // RecyclerView adapter 해제
        binding.recyclerview.adapter = null
        recruitmentAdapter = null
        _binding = null
    }

}