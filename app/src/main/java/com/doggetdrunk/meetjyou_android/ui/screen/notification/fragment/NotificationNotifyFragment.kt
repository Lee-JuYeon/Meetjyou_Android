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
import com.doggetdrunk.meetjyou_android.databinding.FragmentNotificationNotifyBinding
import com.doggetdrunk.meetjyou_android.ui.custom.recyclerview.RecyclerItemGap
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.INotifyHolderClickListener
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.NotifyAdapter
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.recyclerview.NotifyModel
import com.doggetdrunk.meetjyou_android.vm.NotifyVM
import com.doggetdrunk.meetjyou_android.vm.PartyVM
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class NotificationNotifyFragment : Fragment() {

    private var _binding: FragmentNotificationNotifyBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNotificationNotifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding

        setNotificationList(binding.recyclerview)
        observingData()
        loadInitialDetaIfNeed()
    }

    private val notifyVM : NotifyVM by activityViewModels()
    private fun observingData(){
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        notifyVM.notificationList.collectLatest { list ->
                            if (list.isNotEmpty()) {
                                notifyAdapter?.updateList(list)
                            }
                        }
                    }
                }
            }
        }catch (e:Exception){
            Log.e("mException", "")
        }
    }

    private fun loadInitialDetaIfNeed(){
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 병렬로 데이터 로드
                launch { notifyVM.loadNotificationList() }
            } catch (e: Exception) {
                Log.e("mException", "HomeFragment, loadInitialDataIfNeeded // Exception : ${e.localizedMessage}")
            }
        }
    }

    private var notifyAdapter : NotifyAdapter? = null
    private val notifyclickListener = object : INotifyHolderClickListener<NotifyModel>{
        override fun onClick(model: NotifyModel) {
            TODO("Not yet implemented")
        }

        override fun onDelete(model: NotifyModel) {
            TODO("Not yet implemented")
        }
    }

    private val sharedRecyclerViewPool by lazy { RecyclerView.RecycledViewPool() } // ViewPool 재사용을 위한 공유 풀
    private fun setNotificationList(recyclerView: RecyclerView){
        try {
            if (notifyAdapter == null){
                notifyAdapter = NotifyAdapter().apply {
                    setOnClickListener(notifyclickListener)
                }
            }

            recyclerView.apply {
                adapter = notifyAdapter
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
                setItemViewCacheSize(2)

                // ItemDecoration
                addItemDecoration(
                    RecyclerItemGap(
                        verticalSpace = 10,
                        horizontalSpace = 0,
                        includeEdge = false
                    )
                )
            }
        }catch (e:Exception){
            Log.e("mException", "NotificationNotifyFragment, setNotificationList // Exception : ${e.localizedMessage}")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}