package com.doggetdrunk.meetjyou_android.ui.screen.notification.fragment

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
import com.doggetdrunk.meetjyou_android.databinding.FragmentNotificationApplyStatusBinding
import com.doggetdrunk.meetjyou_android.ui.custom.recyclerview.RecyclerItemGap
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview.ApplyAdapter
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview.ApplyModel
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview.ApplyReturnType
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.recyclerview.IApplyHolderClickListener
import com.doggetdrunk.meetjyou_android.vm.NotifyVM
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationApplyStatusFragment : Fragment() {

    private var _binding: FragmentNotificationApplyStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNotificationApplyStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setApplyList()
        observingData()
        loadInitialDataIfNeeded()
    }

    private val notifyVM: NotifyVM by activityViewModels()

    private fun observingData() {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        notifyVM.applyList.collectLatest { list ->
                            if (list.isNotEmpty()) {
                                applyAdapter?.updateList(list)
                                Log.d("ApplyFragment", "Apply list updated: ${list.size} items")
                            } else {
                                Log.d("ApplyFragment", "Apply list is empty")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("mException", "NotificationApplyStatusFragment, observingData // Exception : ${e.localizedMessage}")
        }
    }

    private fun loadInitialDataIfNeeded() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                launch { notifyVM.loadApplyList() }
            } catch (e: Exception) {
                Log.e("mException", "NotificationApplyStatusFragment, loadInitialDataIfNeeded // Exception : ${e.localizedMessage}")
            }
        }
    }

    private var applyAdapter: ApplyAdapter? = null
    private val applyClickListener = object : IApplyHolderClickListener<ApplyModel> {
        override fun onPostClick(model: ApplyModel) {
            Log.d("ApplyFragment", "Apply post clicked: ${model.partyTitle}")
            // TODO: 파티 상세보기 화면으로 이동
        }

        override fun onButtonClicm(type: ApplyReturnType) {
            Log.d("ApplyFragment", "Apply button clicked: $type")
            when (type) {
                ApplyReturnType.WAIT -> {
                    // 대기 중 - 취소 동작
                    Log.d("ApplyFragment", "Cancel apply")
                }
                ApplyReturnType.ACCEPT -> {
                    // 승인됨 - 확인 동작
                    Log.d("ApplyFragment", "Accept confirmed")
                }
                ApplyReturnType.DENIED -> {
                    // 거절됨 - 확인 동작
                    Log.d("ApplyFragment", "Denial confirmed")
                }
            }
        }
    }

    private val sharedRecyclerViewPool by lazy { RecyclerView.RecycledViewPool() }

    private fun setApplyList() {
        try {
            // 현재 레이아웃에 RecyclerView가 없으므로 가상의 recyclerview가 있다고 가정하여 구현
            // 실제로는 fragment_notification_apply_status.xml에 RecyclerView 추가 필요

            if (applyAdapter == null) {
                applyAdapter = ApplyAdapter().apply {
                    setOnClickListener(applyClickListener)
                }
            }

            // 실제 RecyclerView가 있다면 다음과 같이 설정

            binding.recyclerview.apply {
                adapter = applyAdapter
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
                        verticalSpace = 50,
                        horizontalSpace = 0,
                        includeEdge = false
                    )
                )
            }


            Log.d("ApplyFragment", "Apply RecyclerView setup completed")
        } catch (e: Exception) {
            Log.e("mException", "NotificationApplyStatusFragment, setApplyList // Exception : ${e.localizedMessage}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // RecyclerView adapter 해제
        applyAdapter = null
        _binding = null
    }
}