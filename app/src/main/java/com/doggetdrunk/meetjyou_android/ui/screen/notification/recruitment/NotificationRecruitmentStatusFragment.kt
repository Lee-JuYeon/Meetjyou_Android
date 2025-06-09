package com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment

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
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.FragmentNotificationRecruitmentStatusBinding
import com.doggetdrunk.meetjyou_android.ui.custom.bottomsheet.CustomBottomSheet
import com.doggetdrunk.meetjyou_android.ui.custom.popup.CustomPopupView
import com.doggetdrunk.meetjyou_android.ui.custom.recyclerview.RecyclerItemGap
import com.doggetdrunk.meetjyou_android.ui.custom.snackbar.CustomSnackBar
import com.doggetdrunk.meetjyou_android.ui.screen.notification.fragment.PopupRecruitmentProfile
import com.doggetdrunk.meetjyou_android.ui.screen.notification.fragment.SheetRecruitmentProfile
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
    private var sheet_userinfo : SheetRecruitmentProfile? = null
    private var currentBottomSheet: CustomBottomSheet? = null
    private var popup_recruitment : PopupRecruitmentProfile? = null
    private val recruitmentClickListener = object : IRecruitmentHolderClickListener<RecruitmentModel> {
        override fun onProfileClick(model: RecruitmentModel) {
            if (sheet_userinfo == null){
                // NotifyProfileFragment 생성 및 데이터 설정
                sheet_userinfo = SheetRecruitmentProfile().apply {
                    setModel(model)
                }
            }else{
                sheet_userinfo?.apply {
                    setModel(model)
                }
            }

            // CustomBottomSheet 생성 및 표시
            // CustomBottomSheetFragment 생성 및 설정
            val bottomSheetFragment = CustomBottomSheet.newInstance()
                .setTitle("사용자의 프로필")
                .setDraggable(true)
                .setExpandable(true)
                .setContentFragment(sheet_userinfo!!)

            // 표시 (parentFragmentManager 사용)
            bottomSheetFragment.show(parentFragmentManager)
        }

        override fun onContentClick(model: RecruitmentModel) {
            try {
                popup_recruitment = PopupRecruitmentProfile().apply {
                    setModel(model)
                }

                // CustomPopupView 생성
                val customPopup = CustomPopupView.newInstance()
                    .setDismissible(true)
                    .setContentSize(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    .setContentMargins(32, 32, 32, 32)
                    .setBackgroundDimAmount(0.7f)
                    .setCallback(object : CustomPopupView.PopupCallback {
                        override fun onShow() {
                            Log.d("RecruitmentFragment", "Popup shown")
                        }

                        override fun onDismiss() {
                            Log.d("RecruitmentFragment", "Popup dismissed")
                        }
                    })

                // Fragment 설정
                customPopup.setContentFragment(popup_recruitment!!, "recruitment_popup")

                // 표시
                customPopup.showPopup(this@NotificationRecruitmentStatusFragment, "recruitment_popup")


            } catch (e: Exception) {
                Log.e("mException", "NotificationRecruitmentStatusFragment / BottomSheet 표시 중 에러 발생: ${e.message}", e)
                // 에러 발생 시 앱이 크래시되지 않도록 방지
            }
        }
        override fun onHolderClick(model: RecruitmentModel) {
            try {
                popup_recruitment = PopupRecruitmentProfile().apply {
                    setModel(model)
                }

                // CustomPopupView 생성
                val customPopup = CustomPopupView.newInstance()
                    .setDismissible(true)
                    .setContentSize(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    .setContentMargins(32, 32, 32, 32)
                    .setBackgroundDimAmount(0.7f)
                    .setCallback(object : CustomPopupView.PopupCallback {
                        override fun onShow() {
                            Log.d("RecruitmentFragment", "Popup shown")
                        }

                        override fun onDismiss() {
                            Log.d("RecruitmentFragment", "Popup dismissed")
                        }
                    })

                // Fragment 설정
                customPopup.setContentFragment(popup_recruitment!!, "recruitment_popup")

                // 표시
                customPopup.showPopup(this@NotificationRecruitmentStatusFragment, "recruitment_popup")


            } catch (e: Exception) {
                Log.e("mException", "NotificationRecruitmentStatusFragment / BottomSheet 표시 중 에러 발생: ${e.message}", e)
            }
        }
        override fun onDenied(model: RecruitmentModel) {
            notifyVM.handleRecruitmentAction(model, false)

            // CustomSnackBar로 거절 메시지 표시
            CustomSnackBar(
                parentView = requireActivity().findViewById<ViewGroup>(android.R.id.content),
                message = requireContext().getString(R.string.snackbar_recruitment_denied),
                drawableIcon = R.drawable.icon_check_round, // 실제로는 X 아이콘
                drawableIconColour = R.color.snackbar_denied_icon,
                backgroundColour = R.color.snackbar_denied_background
            ).show()

        }

        override fun onAccept(model: RecruitmentModel) {
            notifyVM.handleRecruitmentAction(model, true)

            // CustomSnackBar로 승낙 메시지 표시
            CustomSnackBar(
                parentView = requireActivity().findViewById<ViewGroup>(android.R.id.content),
                message = requireContext().getString(R.string.snackbar_recruitment_accept),
                drawableIcon = R.drawable.icon_check_round, // 실제로는 X 아이콘
                drawableIconColour = R.color.snackbar_accept_icon,
                backgroundColour = R.color.snackbar_accept_background
            ).show()
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
        try {
            // BottomSheet 정리
            currentBottomSheet = null

            // RecyclerView adapter 해제
            binding.recyclerview.adapter = null
            recruitmentAdapter = null
            _binding = null

        } catch (e: Exception) {
            Log.e("mException", "NotificationRecruitmentStatusFragment, onDestroyView // Exception : ${e.localizedMessage}")
        } finally {
            _binding = null
            super.onDestroyView()
        }
    }

}