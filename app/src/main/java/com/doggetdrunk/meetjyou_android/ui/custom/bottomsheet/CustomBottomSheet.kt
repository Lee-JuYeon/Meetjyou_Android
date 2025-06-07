package com.doggetdrunk.meetjyou_android.ui.custom.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.CustomBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * 올바른 방식의 Custom BottomSheet
 * Fragment를 제대로 지원하는 BottomSheetDialogFragment
 */
class CustomBottomSheet : BottomSheetDialogFragment() {

    private var _binding: CustomBottomsheetBinding? = null
    private val binding get() = _binding!!

    // 설정 옵션들
    private var titleText: String = ""
    private var isDraggableEnabled: Boolean = true
    private var isExpandableEnabled: Boolean = true
    private var contentFragment: Fragment? = null
    private var contentFragmentTag: String = "content_fragment"

    companion object {
        fun newInstance(): CustomBottomSheet {
            return CustomBottomSheet()
        }
    }

    /**
     * Builder 패턴으로 설정
     */
    fun setTitle(title: String): CustomBottomSheet {
        this.titleText = title
        return this
    }

    fun setDraggable(draggable: Boolean): CustomBottomSheet {
        this.isDraggableEnabled = draggable
        return this
    }

    fun setExpandable(expandable: Boolean): CustomBottomSheet {
        this.isExpandableEnabled = expandable
        return this
    }

    fun setContentFragment(fragment: Fragment, tag: String = "content_fragment"): CustomBottomSheet {
        this.contentFragment = fragment
        this.contentFragmentTag = tag
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupBehavior()
        addContentFragment()
    }

    private fun setupUI() {
        // 제목 설정
        binding.title.text = titleText

        // 드래그 핸들 표시/숨김
        binding.dragHandle.visibility = if (isDraggableEnabled) View.VISIBLE else View.GONE

        // 드래그 핸들 클릭 이벤트
        binding.dragHandle.setOnClickListener {
            toggleExpanded()
        }
    }

    private fun setupBehavior() {
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as com.google.android.material.bottomsheet.BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )

            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.isDraggable = isDraggableEnabled

                if (!isExpandableEnabled) {
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    behavior.isHideable = false
                }
            }
        }
    }

    private fun addContentFragment() {
        contentFragment?.let { fragment ->
            try {
                childFragmentManager.beginTransaction()
                    .replace(R.id.contentview, fragment, contentFragmentTag)
                    .commit()

                android.util.Log.d("CustomBottomSheet", "Content fragment added successfully")
            } catch (e: Exception) {
                android.util.Log.e("CustomBottomSheet", "Error adding content fragment", e)
            }
        }
    }

    private fun toggleExpanded() {
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = when (behavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> BottomSheetBehavior.STATE_EXPANDED
                    BottomSheetBehavior.STATE_EXPANDED -> BottomSheetBehavior.STATE_COLLAPSED
                    else -> BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    /**
     * 다른 Fragment 내용으로 교체
     */
    fun replaceContentFragment(fragment: Fragment, tag: String = "content_fragment") {
        try {
            childFragmentManager.beginTransaction()
                .replace(R.id.contentview, fragment, tag)
                .commit()
        } catch (e: Exception) {
            android.util.Log.e("CustomBottomSheet", "Error replacing content fragment", e)
        }
    }

    /**
     * Fragment 추가 (스택에 쌓기)
     */
    fun addContentFragment(fragment: Fragment, tag: String, addToBackStack: Boolean = true) {
        try {
            val transaction = childFragmentManager.beginTransaction()
                .add(R.id.contentview, fragment, tag)

            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }

            transaction.commit()
        } catch (e: Exception) {
            android.util.Log.e("CustomBottomSheet", "Error adding content fragment", e)
        }
    }

    /**
     * 편의 메서드: 간편한 표시
     */
    fun show(fragmentManager: FragmentManager) {
        try {
            show(fragmentManager, "custom_bottom_sheet")
        } catch (e: Exception) {
            android.util.Log.e("CustomBottomSheet", "Error showing bottom sheet", e)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}