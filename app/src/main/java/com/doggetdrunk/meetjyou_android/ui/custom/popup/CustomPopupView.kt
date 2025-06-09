package com.doggetdrunk.meetjyou_android.ui.custom.popup
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.internal.ViewUtils.dpToPx

class CustomPopupView : DialogFragment() {

    companion object {
        const val TAG = "CustomPopupView"

        fun newInstance(): CustomPopupView {
            return CustomPopupView()
        }

        // Context에서 자동으로 FragmentManager 찾기
        private fun getFragmentManagerFromContext(context: Context): FragmentManager? {
            return when (context) {
                is FragmentActivity -> context.supportFragmentManager
                else -> {
                    // ContextWrapper나 다른 래퍼 클래스인 경우 재귀적으로 찾기
                    var ctx = context
                    while (ctx is android.content.ContextWrapper) {
                        if (ctx is FragmentActivity) {
                            return ctx.supportFragmentManager
                        }
                        ctx = ctx.baseContext
                    }
                    null
                }
            }
        }

        // Fragment에서 사용할 때 자동으로 적절한 FragmentManager 찾기
        fun getFragmentManagerFromFragment(fragment: Fragment): FragmentManager {
            return fragment.parentFragmentManager
        }

        // 간편한 팝업 생성 및 표시 (Context용) - tag 선택적
        fun showPopup(context: Context, fragment: Fragment, tag: String = TAG): CustomPopupView {
            val popup = newInstance()
            popup.setContentFragment(fragment)
            popup.showPopup(context, tag)
            return popup
        }

        // 간편한 팝업 생성 및 표시 (Fragment용) - tag 선택적
        fun showPopup(parentFragment: Fragment, contentFragment: Fragment, tag: String = TAG): CustomPopupView {
            val popup = newInstance()
            popup.setContentFragment(contentFragment)
            popup.showPopup(parentFragment, tag)
            return popup
        }

        // 가장 간단한 사용법 - tag 없이 (Context용)
        fun showPopup(context: Context, fragment: Fragment): CustomPopupView {
            return showPopup(context, fragment, TAG)
        }

        // 가장 간단한 사용법 - tag 없이 (Fragment용)
        fun showPopup(parentFragment: Fragment, contentFragment: Fragment): CustomPopupView {
            return showPopup(parentFragment, contentFragment, TAG)
        }

        // 빌더 패턴을 위한 정적 메서드
        fun builder(): CustomPopupViewBuilder {
            return CustomPopupViewBuilder()
        }
    }

    private lateinit var contentContainer: FrameLayout
    private val containerId = View.generateViewId()

    // 콜백 인터페이스
    interface PopupCallback {
        fun onDismiss() {}
        fun onShow() {}
    }

    private var callback: PopupCallback? = null
    private var isDismissible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.CENTER)

            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createPopupLayout()
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    private fun createPopupLayout(): View {
        val context = requireContext()

        val rootContainer = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#80000000"))

            setOnClickListener {
                if (isDismissible) {
                    dismiss()
                }
            }
        }

        contentContainer = FrameLayout(context).apply {
            id = containerId
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            ).apply {
                setMargins(48, 48, 48, 48)
            }
            // 코드로 배경 drawable 생성 및 설정
            background = GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.RECTANGLE
                setColor(Color.WHITE) // 흰색 배경
                cornerRadius = dpToPx(context, 10).toFloat() // 20dp 라운드
            }

            // padding 설정 (10dp)
            val paddingPx = dpToPx(context, 20)
            setPadding(paddingPx, paddingPx, paddingPx, paddingPx)

            elevation = 16f

            // 최소 높이 설정으로 fragment가 제대로 표시되도록 함
            minimumHeight = 20

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                outlineProvider = ViewOutlineProvider.BACKGROUND
                clipToOutline = true
            }

            setOnClickListener { /* 이벤트 소비 */ }
        }

        rootContainer.addView(contentContainer)
        return rootContainer
    }

    private var contentFragment: Fragment? = null
    private var contentFragmentTag: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment 추가를 onViewCreated에서 수행
        contentFragment?.let { fragment ->
            addContentFragmentNow(fragment, contentFragmentTag)
        }

        callback?.onShow()
    }

    private fun addContentFragmentNow(fragment: Fragment, tag: String?) {
        try {
            Log.d("CustomPopupView", "Adding fragment: ${fragment::class.simpleName}")

            if (::contentContainer.isInitialized && !childFragmentManager.isStateSaved) {
                childFragmentManager.beginTransaction()
                    .replace(containerId, fragment, tag)
                    .commitNow() // commitNow() 사용으로 즉시 실행

                Log.d("CustomPopupView", "Fragment added successfully")
            }
        } catch (e: Exception) {
            Log.e("CustomPopupView", "Error adding fragment: ${e.message}", e)
        }
    }

    fun setContentFragment(fragment: Fragment, tag: String? = null) {
        this.contentFragment = fragment
        this.contentFragmentTag = tag

        // 이미 View가 생성되었다면 즉시 추가
        if (isAdded && view != null) {
            addContentFragmentNow(fragment, tag)
        }
    }

    fun replaceContentFragment(fragment: Fragment, tag: String? = null, addToBackStack: Boolean = false) {
        if (::contentContainer.isInitialized) {
            val transaction = childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .replace(containerId, fragment, tag)

            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }

            transaction.commit()
        }
    }

    // Context를 받아서 자동으로 표시 (핵심 개선사항)
    fun showPopup(context: Context, tag: String = TAG) {
        val fragmentManager = getFragmentManagerFromContext(context)
        fragmentManager?.let { fm ->
            showSafely(fm, tag)
        } ?: throw IllegalArgumentException("Cannot find FragmentManager from the given context")
    }

    // Fragment를 받아서 자동으로 표시
    fun showPopup(fragment: Fragment, tag: String = TAG) {
        val fragmentManager = getFragmentManagerFromFragment(fragment)
        showSafely(fragmentManager, tag)
    }

    // 기존 방식도 유지 (하위 호환성)
    fun showPopup(fragmentManager: FragmentManager, tag: String = TAG) {
        showSafely(fragmentManager, tag)
    }

    private fun showSafely(fragmentManager: FragmentManager, tag: String) {
        if (fragmentManager.isStateSaved) return

        val existingFragment = fragmentManager.findFragmentByTag(tag)
        if (existingFragment == null && !isAdded && !isVisible) {
            super.show(fragmentManager, tag)
        }
    }

    fun onBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            true
        } else {
            if (isDismissible) {
                dismiss()
            }
            isDismissible
        }
    }

    // 설정 메서드들
    fun setDismissible(dismissible: Boolean): CustomPopupView {
        isDismissible = dismissible
        isCancelable = dismissible
        return this
    }

    fun setCallback(callback: PopupCallback): CustomPopupView {
        this.callback = callback
        return this
    }

    fun setContentSize(width: Int, height: Int): CustomPopupView {
        if (::contentContainer.isInitialized) {
            contentContainer.layoutParams = FrameLayout.LayoutParams(width, height, Gravity.CENTER).apply {
                setMargins(48, 48, 48, 48)
            }
        }
        return this
    }

    fun setContentMargins(left: Int, top: Int, right: Int, bottom: Int): CustomPopupView {
        if (::contentContainer.isInitialized) {
            (contentContainer.layoutParams as? FrameLayout.LayoutParams)?.setMargins(left, top, right, bottom)
        }
        return this
    }

    fun setContentBackground(color: Int): CustomPopupView {
        if (::contentContainer.isInitialized) {
            contentContainer.setBackgroundColor(color)
        }
        return this
    }

    fun setBackgroundDimAmount(alpha: Float): CustomPopupView {
        dialog?.window?.setDimAmount(alpha)
        return this
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        callback?.onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback = null
    }
}

// 확장 함수들
class CustomPopupViewBuilder {
    private val popup = CustomPopupView.newInstance()
    private var fragment: Fragment? = null
    private var fragmentTag: String? = null

    fun setContentFragment(fragment: Fragment, tag: String? = null): CustomPopupViewBuilder {
        this.fragment = fragment
        this.fragmentTag = tag
        return this
    }

    fun setDismissible(dismissible: Boolean): CustomPopupViewBuilder {
        popup.setDismissible(dismissible)
        return this
    }

    fun setCallback(callback: CustomPopupView.PopupCallback): CustomPopupViewBuilder {
        popup.setCallback(callback)
        return this
    }

    fun setContentSize(width: Int, height: Int): CustomPopupViewBuilder {
        popup.setContentSize(width, height)
        return this
    }

    fun setContentMargins(left: Int, top: Int, right: Int, bottom: Int): CustomPopupViewBuilder {
        popup.setContentMargins(left, top, right, bottom)
        return this
    }

    fun setContentBackground(color: Int): CustomPopupViewBuilder {
        popup.setContentBackground(color)
        return this
    }

    fun setBackgroundDimAmount(alpha: Float): CustomPopupViewBuilder {
        popup.setBackgroundDimAmount(alpha)
        return this
    }

    fun build(): CustomPopupView {
        // Fragment를 여기서 설정하지 말고 show할 때 설정
        return popup
    }

    // Context를 받는 showPopup 메서드 - tag 선택적
    fun showPopup(context: Context, tag: String = CustomPopupView.TAG) {
        val builtPopup = build()

        // Fragment 설정을 show 직전에 수행
        this.fragment?.let {
            builtPopup.setContentFragment(it, fragmentTag)
        }

        builtPopup.showPopup(context, tag)
    }

    // Fragment를 받는 showPopup 메서드 - tag 선택적
    fun showPopup(fragment: Fragment, tag: String = CustomPopupView.TAG) {
        val builtPopup = build()

        // Fragment 설정을 show 직전에 수행
        this.fragment?.let {
            builtPopup.setContentFragment(it, fragmentTag)
        }

        builtPopup.showPopup(fragment, tag)
    }

    // 기존 방식도 유지 - tag 선택적
    fun showPopup(fragmentManager: FragmentManager, tag: String = CustomPopupView.TAG) {
        val builtPopup = build()

        // Fragment 설정을 show 직전에 수행
        this.fragment?.let {
            builtPopup.setContentFragment(it, fragmentTag)
        }

        builtPopup.showPopup(fragmentManager, tag)
    }

    // 가장 간단한 사용법 - tag 없이 (Context용)
    fun showPopup(context: Context) {
        showPopup(context, CustomPopupView.TAG)
    }

    // 가장 간단한 사용법 - tag 없이 (Fragment용)
    fun showPopup(fragment: Fragment) {
        showPopup(fragment, CustomPopupView.TAG)
    }
}