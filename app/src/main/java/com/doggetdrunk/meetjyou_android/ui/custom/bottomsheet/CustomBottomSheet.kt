package com.doggetdrunk.meetjyou_android.ui.custom.bottomsheet


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.CustomBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

// 1. 커스텀 BottomSheet 클래스
class CustomBottomSheet private constructor(
    private val context: Context
) {

    companion object {
        fun create(context: Context): CustomBottomSheet {
            return CustomBottomSheet(context)
        }

        fun create(activity: FragmentActivity): CustomBottomSheet {
            return CustomBottomSheet(activity)
        }

        fun create(fragment: Fragment): CustomBottomSheet {
            return CustomBottomSheet(fragment.requireContext())
        }
    }

    // Bottom Sheet 컴포넌트들
    private var dialog: BottomSheetDialog? = null
    private var rootView: View? = null
    private var titleTextView: TextView? = null
    private var contentFrameLayout: FrameLayout? = null
    private var dragHandle: View? = null

    // 설정 옵션들
    private var title: String = ""
    private var isDraggable: Boolean = true
    private var isExpandable: Boolean = true
    private var isCancelable: Boolean = true
    private var peekHeight: Int = BottomSheetBehavior.PEEK_HEIGHT_AUTO

    /**
     * 기본 설정 메서드들
     */
    fun setTitle(title: String): CustomBottomSheet {
        this.title = title
        titleTextView?.text = title
        return this
    }

    fun setDraggable(draggable: Boolean): CustomBottomSheet {
        this.isDraggable = draggable
        return this
    }

    fun setExpandable(expandable: Boolean): CustomBottomSheet {
        this.isExpandable = expandable
        return this
    }

    fun setCancelable(cancelable: Boolean): CustomBottomSheet {
        this.isCancelable = cancelable
        dialog?.setCancelable(cancelable)
        return this
    }

    fun setPeekHeight(height: Int): CustomBottomSheet {
        this.peekHeight = height
        return this
    }

    /**
     * 1. Fragment로 컨텐츠 설정 (가장 유연한 방법)
     */
    fun setContentFragment(fragment: Fragment, tag: String = "bottom_sheet_content"): CustomBottomSheet {
        setupBottomSheetIfNeeded()

        // Fragment에서 FragmentManager 자동 추출
        val fragmentManager = when {
            fragment.parentFragmentManager != null -> fragment.parentFragmentManager
            fragment is Fragment && fragment.requireActivity() is FragmentActivity -> {
                (fragment.requireActivity() as FragmentActivity).supportFragmentManager
            }
            context is FragmentActivity -> context.supportFragmentManager
            else -> null
        }

        fragmentManager?.let { fm ->
            contentFrameLayout?.let { container ->
                val transaction = fm.beginTransaction()
                transaction.replace(container.id, fragment, tag)
                transaction.commit()
            }
        } ?: run {
            throw IllegalStateException("FragmentManager를 찾을 수 없습니다. FragmentActivity context가 필요합니다.")
        }

        return this
    }

    /**
     * Fragment 클래스로 컨텐츠 설정 (더 유연한 방법)
     */
    fun <T : Fragment> setContentFragment(
        fragmentClass: Class<T>,
        args: Bundle? = null,
        tag: String = "bottom_sheet_content"
    ): CustomBottomSheet {
        setupBottomSheetIfNeeded()

        val fragmentManager = when (context) {
            is FragmentActivity -> context.supportFragmentManager
            else -> throw IllegalStateException("Fragment 사용을 위해서는 FragmentActivity context가 필요합니다.")
        }

        contentFrameLayout?.let { container ->
            val fragment = fragmentClass.newInstance().apply {
                arguments = args
            }

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(container.id, fragment, tag)
            transaction.commit()
        }

        return this
    }

    /**
     * 인라인 Fragment 생성자로 컨텐츠 설정
     */
    inline fun <reified T : Fragment> setContentFragment(
        args: Bundle? = null,
        tag: String = "bottom_sheet_content"
    ): CustomBottomSheet {
        return setContentFragment(T::class.java, args, tag)
    }

    /**
     * 2. Layout 리소스로 컨텐츠 설정
     */
    fun setContentLayout(layoutResId: Int, setupView: ((View) -> Unit)? = null): CustomBottomSheet {
        setupBottomSheetIfNeeded()

        contentFrameLayout?.let { container ->
            val inflatedView = LayoutInflater.from(context).inflate(layoutResId, container, false)
            container.removeAllViews()
            container.addView(inflatedView)
            setupView?.invoke(inflatedView)
        }

        return this
    }

    /**
     * 3. 직접 View로 컨텐츠 설정
     */
    fun setContentView(view: View): CustomBottomSheet {
        setupBottomSheetIfNeeded()

        contentFrameLayout?.let { container ->
            // 기존 parent에서 제거
            (view.parent as? ViewGroup)?.removeView(view)

            container.removeAllViews()
            container.addView(view)
        }

        return this
    }

    /**
     * 4. 동적 뷰 빌더로 컨텐츠 설정
     */
    fun setContentBuilder(builder: (FrameLayout) -> Unit): CustomBottomSheet {
        setupBottomSheetIfNeeded()

        contentFrameLayout?.let { container ->
            container.removeAllViews()
            builder(container)
        }

        return this
    }

    /**
     * Bottom Sheet 표시
     */
    fun show(): BottomSheetDialog? {
        setupBottomSheetIfNeeded()

        return dialog?.apply {
            if (!isShowing) {
                show()
                setupBehavior()
            }
        }
    }

    /**
     * Bottom Sheet 숨기기
     */
    fun dismiss() {
        dialog?.dismiss()
    }

    /**
     * Bottom Sheet 완전 제거
     */
    fun destroy() {
        dialog?.dismiss()
        dialog = null
        rootView = null
        titleTextView = null
        contentFrameLayout = null
        dragHandle = null
    }

    /**
     * 현재 표시 상태 확인
     */
    fun isShowing(): Boolean = dialog?.isShowing == true

    /**
     * 내부 메서드: Bottom Sheet 초기 설정
     */
    private fun setupBottomSheetIfNeeded() {
        if (dialog == null) {
            createBottomSheet()
        }
    }

    /**
     * 내부 메서드: Bottom Sheet 생성
     */
    private fun createBottomSheet() {
        // Root view 생성 (제공된 XML 기반)
        rootView = LayoutInflater.from(context).inflate(R.layout.custom_bottomsheet, null)

        // 뷰 참조 획득
        titleTextView = rootView?.findViewById(R.id.title)
        contentFrameLayout = rootView?.findViewById(R.id.contentview)
        dragHandle = rootView?.findViewById(R.id.drag_handle)

        // 초기 설정 적용
        titleTextView?.text = title
        dragHandle?.visibility = if (isDraggable) View.VISIBLE else View.GONE

        // BottomSheetDialog 생성
        dialog = BottomSheetDialog(context).apply {
            setContentView(rootView!!)
            setCancelable(isCancelable)
        }
    }

    /**
     * 내부 메서드: BottomSheetBehavior 설정
     */
    private fun setupBehavior() {
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)

                behavior.apply {
                    peekHeight = this@CustomBottomSheet.peekHeight
                    isDraggable = this@CustomBottomSheet.isDraggable

                    if (!isExpandable) {
                        state = BottomSheetBehavior.STATE_COLLAPSED
                        isHideable = false
                    }

                    // 드래그 핸들 터치 이벤트
                    dragHandle?.setOnClickListener {
                        state = when (state) {
                            BottomSheetBehavior.STATE_COLLAPSED -> BottomSheetBehavior.STATE_EXPANDED
                            BottomSheetBehavior.STATE_EXPANDED -> BottomSheetBehavior.STATE_COLLAPSED
                            else -> BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                }
            }
        }
    }

    /**
     * 편의 메서드들
     */
    fun getDialog(): BottomSheetDialog? = dialog
    fun getRootView(): View? = rootView
    fun getContentContainer(): FrameLayout? = contentFrameLayout
    fun getTitleView(): TextView? = titleTextView
}

